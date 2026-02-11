package warpnetclient

import (
	"context"
	"crypto/rand"
	"fmt"
	"io"
	"sync"
	"time"

	"github.com/libp2p/go-libp2p"
	"github.com/libp2p/go-libp2p/core/crypto"
	"github.com/libp2p/go-libp2p/core/host"
	"github.com/libp2p/go-libp2p/core/network"
	"github.com/libp2p/go-libp2p/core/peer"
	"github.com/libp2p/go-libp2p/core/peerstore"
	"github.com/libp2p/go-libp2p/core/protocol"
	noise "github.com/libp2p/go-libp2p/p2p/security/noise"
	"github.com/libp2p/go-libp2p/p2p/transport/tcp"
	pnet "github.com/libp2p/go-libp2p-pnet"
	"github.com/multiformats/go-multiaddr"
)

// WarpNetClient represents a thin libp2p client for connecting to desktop nodes
type WarpNetClient struct {
	host      host.Host
	ctx       context.Context
	cancel    context.CancelFunc
	desktopID peer.ID
	mu        sync.RWMutex
}

// NewClient creates a new WarpNet thin client configured as per requirements
// psk: optional pre-shared key for private network (32 bytes), can be nil
func NewClient(psk []byte) (*WarpNetClient, error) {
	ctx, cancel := context.WithCancel(context.Background())

	// Generate a new private key for this client instance
	// Ed25519 has a fixed key size, so -1 is used when the parameter is not applicable
	privKey, _, err := crypto.GenerateKeyPairWithReader(crypto.Ed25519, -1, rand.Reader)
	if err != nil {
		cancel()
		return nil, fmt.Errorf("failed to generate key pair: %w", err)
	}

	// Build libp2p options matching thin client requirements
	opts := []libp2p.Option{
		libp2p.Identity(privKey),           // Client identity
		libp2p.NoListenAddrs,               // Client-only mode - no listening
		libp2p.DisableMetrics(),            // Lightweight
		libp2p.DisableRelay(),              // No relay listening
		libp2p.Ping(true),                  // Enable ping for connectivity checks
		libp2p.Security(noise.ID, noise.New), // Noise protocol for encryption
		libp2p.Transport(tcp.NewTCPTransport), // TCP transport
		libp2p.UserAgent("warpnet-android/1.0"), // Custom user agent
		libp2p.DisableIdentifyAddressDiscovery(), // Disable address discovery (client-only)
	}

	// Add PSK if provided for private network support
	if psk != nil && len(psk) == 32 {
		opts = append(opts, libp2p.PrivateNetwork(pnet.NewProtector(psk)))
	} else if psk != nil {
		cancel()
		return nil, fmt.Errorf("PSK must be exactly 32 bytes, got %d bytes", len(psk))
	}

	// Create the libp2p host
	h, err := libp2p.New(opts...)
	if err != nil {
		cancel()
		return nil, fmt.Errorf("failed to create libp2p host: %w", err)
	}

	return &WarpNetClient{
		host:   h,
		ctx:    ctx,
		cancel: cancel,
	}, nil
}

// Connect establishes a connection to the desktop WarpNet node
// peerIDStr: base58-encoded peer ID of the desktop node
// addrStr: multiaddr string (e.g., "/ip4/192.168.1.100/tcp/4001")
func (c *WarpNetClient) Connect(peerIDStr string, addrStr string) error {
	c.mu.Lock()
	defer c.mu.Unlock()

	// Parse peer ID
	pid, err := peer.Decode(peerIDStr)
	if err != nil {
		return fmt.Errorf("invalid peer ID: %w", err)
	}

	// Parse multiaddr
	maddr, err := multiaddr.NewMultiaddr(addrStr)
	if err != nil {
		return fmt.Errorf("invalid multiaddr: %w", err)
	}

	// Add peer to peerstore with permanent TTL
	c.host.Peerstore().AddAddr(pid, maddr, peerstore.PermanentAddrTTL)

	// Establish connection to the peer
	ctx, cancel := context.WithTimeout(c.ctx, 30*time.Second)
	defer cancel()

	if err := c.host.Connect(ctx, peer.AddrInfo{ID: pid}); err != nil {
		return fmt.Errorf("connection failed: %w", err)
	}

	c.desktopID = pid
	return nil
}

// SendMessage sends a message to the desktop node using a specific protocol
// protocolID: the libp2p protocol ID (e.g., "/warpnet/api/feed/1.0.0")
// data: the message payload as bytes
// Returns the response bytes
func (c *WarpNetClient) SendMessage(protocolID string, data []byte) ([]byte, error) {
	c.mu.RLock()
	desktopID := c.desktopID
	c.mu.RUnlock()

	if desktopID == "" {
		return nil, fmt.Errorf("not connected to desktop node")
	}

	// Open a new stream to the desktop node
	ctx, cancel := context.WithTimeout(c.ctx, 30*time.Second)
	defer cancel()

	stream, err := c.host.NewStream(ctx, desktopID, protocol.ID(protocolID))
	if err != nil {
		return nil, fmt.Errorf("failed to open stream: %w", err)
	}
	defer stream.Close()

	// Set deadlines for read/write
	stream.SetDeadline(time.Now().Add(30 * time.Second))

	// Write the request data
	if _, err := stream.Write(data); err != nil {
		return nil, fmt.Errorf("failed to write data: %w", err)
	}

	// Close write side to signal end of request
	if err := stream.CloseWrite(); err != nil {
		return nil, fmt.Errorf("failed to close write: %w", err)
	}

	// Read the response
	response, err := io.ReadAll(stream)
	if err != nil {
		return nil, fmt.Errorf("failed to read response: %w", err)
	}

	return response, nil
}

// GetPeerID returns the client's own peer ID
func (c *WarpNetClient) GetPeerID() string {
	return c.host.ID().String()
}

// IsConnected checks if connected to the desktop node
func (c *WarpNetClient) IsConnected() bool {
	c.mu.RLock()
	defer c.mu.RUnlock()

	if c.desktopID == "" {
		return false
	}

	connectedness := c.host.Network().Connectedness(c.desktopID)
	return connectedness == network.Connected
}

// Disconnect closes the connection to the desktop node
func (c *WarpNetClient) Disconnect() error {
	c.mu.Lock()
	defer c.mu.Unlock()

	if c.desktopID != "" {
		if err := c.host.Network().ClosePeer(c.desktopID); err != nil {
			return fmt.Errorf("failed to close peer connection: %w", err)
		}
		c.desktopID = ""
	}

	return nil
}

// Close shuts down the entire libp2p host
func (c *WarpNetClient) Close() error {
	c.cancel()
	return c.host.Close()
}
