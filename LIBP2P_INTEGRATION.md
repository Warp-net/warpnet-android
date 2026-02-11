# libp2p Integration Guide for WarpNet Android

This document explains the libp2p integration in the WarpNet Android app using **native Go libp2p** compiled to a native library via **gomobile**.

## Architecture Overview

The WarpNet Android app uses a native Go implementation of libp2p, compiled to a native Android library (.aar) using gomobile. This provides:

- Full Go libp2p compatibility
- Noise protocol encryption
- PSK support for private networks
- Thin client architecture (no listening, client-only mode)
- Direct integration with WarpNet desktop node code

```
┌──────────────────────────────────┐
│  Android App (Kotlin)            │
│  ┌────────────────────────────┐  │
│  │   LibP2PClient.kt          │  │
│  │   (JNI wrapper)            │  │
│  └───────────┬────────────────┘  │
│              │                    │
└──────────────┼────────────────────┘
               │ JNI / gomobile
┌──────────────┼────────────────────┐
│              ▼                    │
│  Native Library (warpnet.aar)     │
│  ┌────────────────────────────┐  │
│  │   Go libp2p Implementation │  │
│  │   - Noise Security         │  │
│  │   - TCP Transport          │  │
│  │   - PSK Support            │  │
│  │   - Client-only mode       │  │
│  └────────────────────────────┘  │
└───────────────────────────────────┘
```

## Dependencies

### Build-time Dependencies

1. **Go 1.21+**: For building the native library
2. **gomobile**: For compiling Go to Android native library
3. **Android SDK & NDK**: For Android build support

### Installation

```bash
# Install Go (if not already installed)
# Visit https://golang.org/dl/

# Install gomobile
go install golang.org/x/mobile/cmd/gomobile@latest

# Initialize gomobile
gomobile init
```

### Go Module Dependencies

The native Go module (`native/warpnet-client`) uses:

```go
require (
    github.com/libp2p/go-libp2p v0.35.0
    github.com/libp2p/go-libp2p-pnet v0.2.0
    github.com/multiformats/go-multiaddr v0.12.4
)
```

These are automatically downloaded when building.

## Implementation Details

### Native Go Client (`native/warpnet-client/client.go`)

The core libp2p client is implemented in Go with the following features:

```go
// Thin client configuration matching requirements
opts := []libp2p.Option{
    libp2p.Identity(privKey),              // Client identity
    libp2p.NoListenAddrs,                  // Client-only mode - no listening
    libp2p.DisableMetrics(),               // Lightweight
    libp2p.DisableRelay(),                 // No relay listening
    libp2p.Ping(true),                     // Enable ping
    libp2p.Security(noise.ID, noise.New),  // Noise protocol encryption
    libp2p.Transport(tcp.NewTCPTransport), // TCP transport
    libp2p.UserAgent("warpnet-android/1.0"), // Custom user agent
    libp2p.DisableIdentifyAddressDiscovery(), // Disable discovery
}

// Add PSK if provided for private network
if psk != nil && len(psk) == 32 {
    opts = append(opts, libp2p.PrivateNetwork(pnet.NewProtector(psk)))
}
```

### Gomobile Wrapper (`native/warpnet-client/mobile.go`)

The mobile wrapper provides JNI-compatible functions:

- `Initialize(pskBase64 string) string` - Create client with optional PSK
- `ConnectToNode(peerID, address string) string` - Connect to desktop node
- `SendRequest(protocolID, dataJSON string) string` - Send API request
- `GetClientPeerID() string` - Get client's peer ID
- `CheckConnection() string` - Check connection status
- `DisconnectFromNode() string` - Disconnect from node
- `Shutdown() string` - Shutdown client

### Kotlin JNI Bridge (`app/.../LibP2PClient.kt`)

The Kotlin layer wraps the native functions:

```kotlin
// Load native library
init {
    System.loadLibrary("gojni")
}

// Initialize native client
val initError = Warpnetclient.initialize(pskBase64)

// Connect to desktop node
val connectError = Warpnetclient.connectToNode(config.peerId, address)

// Send API request
val responseJSON = Warpnetclient.sendRequest(endpoint, data)
```

## Configuration Matching Requirements

The Go implementation fully matches the pseudocode requirements:

| Requirement | Implementation | Status |
|------------|----------------|--------|
| `libp2p.Identity(privKey)` | ✅ Generated Ed25519 key | Complete |
| `libp2p.NoListenAddrs` | ✅ Client-only mode, no listening | Complete |
| `libp2p.DisableMetrics()` | ✅ Metrics disabled | Complete |
| `libp2p.DisableRelay()` | ✅ Relay disabled | Complete |
| `libp2p.Ping(true)` | ✅ Ping enabled | Complete |
| `libp2p.Security(noise.ID, noise.New)` | ✅ Noise protocol encryption | Complete |
| `libp2p.Transport(tcp.NewTCPTransport)` | ✅ TCP transport | Complete |
| `libp2p.PrivateNetwork(pnet.PSK(psk))` | ✅ PSK support for private networks | Complete |
| `libp2p.UserAgent("warpnet-android")` | ✅ Custom user agent | Complete |
| `libp2p.DisableIdentifyAddressDiscovery()` | ✅ Discovery disabled | Complete |

## Building the Native Library

### Step 1: Build Script

Run the provided build script:

```bash
chmod +x scripts/build-native.sh
./scripts/build-native.sh
```

### Step 2: Manual Build (Alternative)

```bash
cd native/warpnet-client

# Download dependencies
go mod download
go mod tidy

# Build for Android
gomobile bind -v -target=android -o ../../app/libs/warpnet.aar .
```

### Step 3: Verify Build

The `warpnet.aar` file should be created in `app/libs/`:

```bash
ls -lh app/libs/warpnet.aar
```

## Testing

### Go Unit Tests

Test the native Go client:

```bash
cd native/warpnet-client
go test -v
```

### Android Integration Tests

Test the Android app with the native library:

```kotlin
@Test
fun testNativeClientConnection() = runBlocking {
    val config = NodeConfig(
        peerId = "12D3KooW...",
        lanAddress = "/ip4/192.168.1.100/tcp/4001",
        sessionToken = "test-token"
    )
    
    val client = LibP2PClient.getInstance()
    val result = client.connect(config)
    
    assertTrue(result.isSuccess)
    assertEquals(ConnectionStatus.CONNECTED, client.getStatus())
}
```

## Advantages of Go libp2p via gomobile

1. **Full Compatibility**: Uses official Go libp2p implementation
2. **Feature Complete**: All libp2p features available (Noise, PSK, etc.)
3. **Proven**: Same codebase as desktop WarpNet node
4. **Security**: Battle-tested Go cryptography
5. **Maintainability**: Shares code with desktop implementation
6. **Standards Compliant**: Full libp2p spec compliance

## Resources

- [go-libp2p GitHub](https://github.com/libp2p/go-libp2p)
- [gomobile Documentation](https://pkg.go.dev/golang.org/x/mobile)
- [libp2p Specifications](https://github.com/libp2p/specs)
- [WarpNet Desktop Node](https://github.com/Warp-net/warpnet)
- [Noise Protocol](https://noiseprotocol.org/)

## Desktop Node Requirements

The desktop WarpNet node must:

1. **Listen for connections** from mobile clients
2. **Accept incoming streams** for API requests
3. **Implement protocols**:
   - `/warpnet/api/feed/1.0.0` - Feed retrieval
   - `/warpnet/api/post/1.0.0` - Post creation
   - `/warpnet/api/notifications/1.0.0` - Notifications
   - `/warpnet/api/messages/1.0.0` - Messages
   - And other API endpoints as defined in DESKTOP_API_SPEC.md

4. **Generate QR codes** with connection info:
   ```json
   {
     "peerId": "12D3KooW...",
     "addresses": ["/ip4/192.168.1.100/tcp/4001"],
     "sessionToken": "base64-token",
     "psk": "base64-psk"
   }
   ```

5. **Authenticate sessions** using session tokens
6. **Support Noise encryption** on all streams
7. **Optional PSK** for private network isolation

## Security Considerations

1. **Session Tokens**: Should expire after reasonable time (e.g., 24 hours)
2. **PSK Storage**: Store PSK securely using Android's EncryptedSharedPreferences
3. **Peer ID Pinning**: Verify desktop node's peer ID matches expected value
4. **Transport Security**: All communication encrypted with Noise protocol
5. **Network Security**: Support both LAN and relay connections

## Troubleshooting

### Build Issues

**Problem**: `gomobile not found`
```bash
go install golang.org/x/mobile/cmd/gomobile@latest
gomobile init
```

**Problem**: `Android SDK/NDK not found`
- Set `ANDROID_HOME` environment variable
- Install NDK via Android Studio

**Problem**: `Go dependencies fail to download`
```bash
cd native/warpnet-client
go clean -modcache
go mod download
```

### Runtime Issues

**Problem**: `UnsatisfiedLinkError: couldn't find "libgojni.so"`
- Ensure `warpnet.aar` was built successfully
- Check that AAR contains native libraries for your device's architecture
- Rebuild with: `gomobile bind -target=android/arm64,android/amd64 ...`

**Problem**: `Connection failed`
- Verify desktop node is running and reachable
- Check firewall settings
- Verify peer ID and multiaddr are correct
- Check network connectivity

## Next Steps

1. **Build the native library** using the provided scripts
2. **Test on a device** with a running desktop node
3. **Implement desktop node API** according to DESKTOP_API_SPEC.md
4. **Add comprehensive error handling**
5. **Implement reconnection logic** for mobile network changes
6. **Add background service** for maintaining connection
7. **Implement notification handling** for incoming messages

## File Structure

```
warpnet-android/
├── native/
│   └── warpnet-client/           # Go libp2p implementation
│       ├── client.go             # Core client logic
│       ├── mobile.go             # Gomobile wrapper
│       ├── go.mod                # Go dependencies
│       └── README.md             # Build instructions
├── app/
│   ├── libs/
│   │   └── warpnet.aar          # Built native library (generated)
│   ├── src/main/java/net/warp/android/
│   │   └── network/
│   │       └── LibP2PClient.kt  # JNI bridge
│   └── build.gradle             # Android dependencies
├── scripts/
│   └── build-native.sh          # Native build script
└── LIBP2P_INTEGRATION.md        # This file
```
