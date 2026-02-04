# libp2p Integration Guide for WarpNet Android

This document explains how to integrate native libp2p functionality into the WarpNet Android app.

## Current State

The current implementation includes placeholder methods in `LibP2PClient.kt` that simulate the libp2p connection. These need to be replaced with actual native Go code compiled for Android using gomobile.

## Architecture

```
┌──────────────────────────────────┐
│  Android App (Kotlin/Java)       │
│  ┌────────────────────────────┐  │
│  │   LibP2PClient.kt          │  │
│  │   (JNI/Gomobile Bridge)    │  │
│  └───────────┬────────────────┘  │
│              │                    │
└──────────────┼────────────────────┘
               │ JNI
┌──────────────┼────────────────────┐
│              ▼                    │
│  Native Library (.so)             │
│  ┌────────────────────────────┐  │
│  │   libp2p Go Implementation │  │
│  │   - Noise Security         │  │
│  │   - TCP Transport          │  │
│  │   - Private Network (PSK)  │  │
│  └────────────────────────────┘  │
└───────────────────────────────────┘
```

## Implementation Steps

### 1. Create Go Module for libp2p

Create a new Go module in `native/warpnet-client/`:

```go
package warpnetclient

import (
    "context"
    "crypto/rand"
    "fmt"
    
    "github.com/libp2p/go-libp2p"
    "github.com/libp2p/go-libp2p/core/crypto"
    "github.com/libp2p/go-libp2p/core/host"
    "github.com/libp2p/go-libp2p/core/peer"
    noise "github.com/libp2p/go-libp2p/p2p/security/noise"
    "github.com/libp2p/go-libp2p/p2p/transport/tcp"
    pnet "github.com/libp2p/go-libp2p-pnet"
)

type WarpNetClient struct {
    host host.Host
    ctx  context.Context
}

// NewClient creates a new libp2p client configured as per requirements
func NewClient(psk []byte) (*WarpNetClient, error) {
    ctx := context.Background()
    
    // Generate a new private key for this client
    privKey, _, err := crypto.GenerateKeyPairWithReader(crypto.Ed25519, 2048, rand.Reader)
    if err != nil {
        return nil, err
    }
    
    // Create libp2p options matching the pseudocode from the issue
    opts := []libp2p.Option{
        libp2p.Identity(privKey),
        libp2p.NoListenAddrs,                          // Client-only mode
        libp2p.DisableMetrics(),                       // Lightweight
        libp2p.DisableRelay(),                         // No relay listening
        libp2p.Ping(true),                             // Enable ping
        libp2p.Security(noise.ID, noise.New),          // Noise protocol
        libp2p.Transport(tcp.NewTCPTransport),         // TCP transport
        libp2p.UserAgent("warpnet-android"),           // Custom user agent
    }
    
    // Add PSK if provided
    if psk != nil {
        opts = append(opts, libp2p.PrivateNetwork(pnet.PSK(psk)))
    }
    
    // Create the libp2p host
    h, err := libp2p.New(opts...)
    if err != nil {
        return nil, err
    }
    
    return &WarpNetClient{
        host: h,
        ctx:  ctx,
    }, nil
}

// Connect establishes a connection to the desktop node
func (c *WarpNetClient) Connect(peerID string, addr string) error {
    // Parse peer ID
    pid, err := peer.Decode(peerID)
    if err != nil {
        return fmt.Errorf("invalid peer ID: %w", err)
    }
    
    // Parse multiaddr
    maddr, err := multiaddr.NewMultiaddr(addr)
    if err != nil {
        return fmt.Errorf("invalid multiaddr: %w", err)
    }
    
    // Add peer to peerstore
    c.host.Peerstore().AddAddr(pid, maddr, peerstore.PermanentAddrTTL)
    
    // Connect to the peer
    if err := c.host.Connect(c.ctx, peer.AddrInfo{ID: pid}); err != nil {
        return fmt.Errorf("connection failed: %w", err)
    }
    
    return nil
}

// SendMessage sends a message to the desktop node
func (c *WarpNetClient) SendMessage(endpoint string, data []byte) ([]byte, error) {
    // Implementation would use libp2p streams to send/receive data
    // This is a placeholder
    return nil, nil
}

// Close closes the libp2p host
func (c *WarpNetClient) Close() error {
    return c.host.Close()
}
```

### 2. Build with Gomobile

Install gomobile:
```bash
go install golang.org/x/mobile/cmd/gomobile@latest
gomobile init
```

Build the native library for Android:
```bash
cd native/warpnet-client
gomobile bind -target=android -o ../../app/libs/warpnet.aar .
```

This creates an AAR (Android Archive) that can be imported into the Android project.

### 3. Update Android Build Configuration

Modify `app/build.gradle`:

```gradle
android {
    // ... existing configuration ...
    
    sourceSets {
        main {
            jniLibs.srcDirs = ['libs']
        }
    }
}

dependencies {
    // ... existing dependencies ...
    
    // Add the generated AAR
    implementation files('libs/warpnet.aar')
}
```

### 4. Update LibP2PClient.kt

Replace the placeholder methods with actual JNI calls:

```kotlin
package net.warp.android.network

import warpnetclient.Warpnetclient
import warpnetclient.WarpNetClient

class LibP2PClient {
    private var nativeClient: WarpNetClient? = null
    
    private fun initializeNativeClient(config: NodeConfig): Boolean {
        return try {
            nativeClient = Warpnetclient.newClient(config.psk)
            
            // Determine which address to use
            val address = when {
                config.useRelay && config.relayAddress != null -> config.relayAddress
                config.lanAddress != null -> config.lanAddress
                config.remoteAddress != null -> config.remoteAddress
                else -> return false
            }
            
            nativeClient?.connect(config.peerId, address)
            true
        } catch (e: Exception) {
            Log.e(TAG, "Native client initialization failed", e)
            false
        }
    }
    
    private fun sendToNativeClient(endpoint: String, data: String): String {
        return try {
            val response = nativeClient?.sendMessage(endpoint, data.toByteArray())
            String(response ?: byteArrayOf())
        } catch (e: Exception) {
            Log.e(TAG, "Send failed", e)
            throw e
        }
    }
    
    private fun shutdownNativeClient() {
        nativeClient?.close()
        nativeClient = null
    }
}
```

## Testing

### Unit Tests

Create unit tests for the Go code:

```go
func TestClientCreation(t *testing.T) {
    client, err := NewClient(nil)
    if err != nil {
        t.Fatalf("Failed to create client: %v", err)
    }
    defer client.Close()
    
    if client.host == nil {
        t.Fatal("Host is nil")
    }
}

func TestClientWithPSK(t *testing.T) {
    psk := make([]byte, 32)
    rand.Read(psk)
    
    client, err := NewClient(psk)
    if err != nil {
        t.Fatalf("Failed to create client with PSK: %v", err)
    }
    defer client.Close()
}
```

### Integration Tests

Test the connection to a local WarpNet node:

```kotlin
@Test
fun testConnectionToLocalNode() = runBlocking {
    val config = NodeConfig(
        peerId = "12D3KooWTest...",
        lanAddress = "/ip4/192.168.1.100/tcp/4001",
        sessionToken = "test-token"
    )
    
    val client = LibP2PClient.getInstance()
    val result = client.connect(config)
    
    assertTrue(result.isSuccess)
    assertEquals(ConnectionStatus.CONNECTED, client.getStatus())
}
```

## Desktop Node Requirements

The desktop WarpNet node must:

1. **Expose an API** for mobile clients:
   - Feed retrieval
   - Post creation
   - Notifications
   - Messages

2. **Generate QR codes** containing:
   ```json
   {
     "peerId": "12D3KooW...",
     "addresses": [
       "/ip4/192.168.1.100/tcp/4001",
       "/ip4/public.ip/tcp/4001"
     ],
     "sessionToken": "base64-encoded-token",
     "psk": "base64-encoded-psk"
   }
   ```

3. **Authenticate mobile clients** using session tokens

4. **Support libp2p streams** for bidirectional communication

## Security Considerations

1. **Session Tokens**: Should expire after a reasonable time
2. **PSK Rotation**: Consider rotating PSKs periodically
3. **Certificate Pinning**: Pin the desktop node's peer ID
4. **Secure Storage**: Store PSK and tokens securely on Android (EncryptedSharedPreferences)

## Alternative: Pure Go Implementation

Instead of gomobile, you could:

1. Build a standalone Go binary for Android
2. Run it as a background service
3. Use IPC (sockets, intent services) to communicate

This avoids gomobile complexity but adds other challenges.

## Dependencies

Required Go modules:
```
github.com/libp2p/go-libp2p
github.com/libp2p/go-libp2p/core
github.com/libp2p/go-libp2p/p2p/security/noise
github.com/libp2p/go-libp2p/p2p/transport/tcp
github.com/libp2p/go-libp2p-pnet
```

## References

- [libp2p Documentation](https://docs.libp2p.io/)
- [gomobile Documentation](https://pkg.go.dev/golang.org/x/mobile)
- [libp2p Examples](https://github.com/libp2p/go-libp2p/tree/master/examples)
- [Noise Protocol](https://noiseprotocol.org/)
