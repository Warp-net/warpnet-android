# WarpNet Android Native Client

This directory contains the Go implementation of the libp2p client for WarpNet Android.

## Overview

The WarpNet client is built using Go and libp2p, compiled to a native Android library (.aar) using gomobile.

## Structure

- `client.go` - Core libp2p client implementation
- `mobile.go` - Gomobile-compatible wrapper for JNI binding
- `go.mod` - Go module dependencies

## Building

### Prerequisites

1. Go 1.21 or higher
2. gomobile tool
3. Android SDK and NDK

### Setup gomobile

```bash
go install golang.org/x/mobile/cmd/gomobile@latest
gomobile init
```

### Build for Android

```bash
# From this directory
gomobile bind -target=android -o ../../app/libs/warpnet.aar .
```

This generates `warpnet.aar` which can be included in the Android project.

### Build for specific architectures

```bash
# For specific ABIs
gomobile bind -target=android/arm64,android/amd64 -o ../../app/libs/warpnet.aar .
```

## API

The mobile wrapper provides these functions for use from Kotlin/Java:

### Initialize(pskBase64 string) string
Creates a new client with optional PSK. Returns error message or empty string.

### ConnectToNode(peerID string, address string) string
Connects to desktop node. Returns error message or empty string.

### SendRequest(protocolID string, dataJSON string) string
Sends a request to desktop node. Returns JSON response.

### GetClientPeerID() string
Returns the client's peer ID.

### CheckConnection() string
Returns "true" or "false" based on connection status.

### DisconnectFromNode() string
Disconnects from desktop node. Returns error message or empty string.

### Shutdown() string
Shuts down the client. Returns error message or empty string.

## Configuration

The client is configured as a thin client matching WarpNet requirements:

- `libp2p.Identity(privKey)` - Generated Ed25519 key
- `libp2p.NoListenAddrs` - Client-only, no listening
- `libp2p.DisableMetrics()` - Lightweight
- `libp2p.DisableRelay()` - No relay functionality
- `libp2p.Ping(true)` - Ping enabled
- `libp2p.Security(noise.ID, noise.New)` - Noise protocol encryption
- `libp2p.Transport(tcp.NewTCPTransport)` - TCP transport
- `libp2p.UserAgent("warpnet-android/1.0")` - Custom user agent
- `libp2p.PrivateNetwork(pnet.NewProtector(psk))` - Optional PSK for private networks

## Testing

Run Go tests:

```bash
go test -v
```

## Dependencies

Main dependencies are declared in `go.mod`:

- `github.com/libp2p/go-libp2p` - Core libp2p implementation
- `github.com/libp2p/go-libp2p-pnet` - Private network support
- `github.com/multiformats/go-multiaddr` - Multiaddr support
