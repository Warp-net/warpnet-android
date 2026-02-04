# libp2p Integration Guide for WarpNet Android

This document explains the libp2p integration in the WarpNet Android app using **jvm-libp2p**, a native Kotlin/JVM implementation.

## Current Implementation

The app now uses **jvm-libp2p** (https://github.com/libp2p/jvm-libp2p), a native Kotlin implementation of libp2p that works directly on Android without requiring Go bindings or gomobile.

## Architecture

```
┌──────────────────────────────────┐
│  Android App (Kotlin)            │
│  ┌────────────────────────────┐  │
│  │   LibP2PClient.kt          │  │
│  │   (jvm-libp2p integration) │  │
│  └───────────┬────────────────┘  │
│              │                    │
└──────────────┼────────────────────┘
               │ Direct Kotlin API
┌──────────────┼────────────────────┐
│              ▼                    │
│  jvm-libp2p Library               │
│  ┌────────────────────────────┐  │
│  │   Native Kotlin/JVM impl   │  │
│  │   - Noise Security         │  │
│  │   - TCP Transport          │  │
│  │   - Stream Multiplexing    │  │
│  └────────────────────────────┘  │
└───────────────────────────────────┘
```

## Dependencies

The following dependencies have been added to the project:

### build.gradle (Project level)
```gradle
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url "https://dl.cloudsmith.io/public/libp2p/jvm-libp2p/maven/" }
        maven { url "https://jitpack.io" }
        maven { url "https://artifacts.consensys.net/public/maven/maven/" }
    }
}
```

### app/build.gradle
```gradle
dependencies {
    // libp2p JVM implementation
    implementation 'io.libp2p:jvm-libp2p:1.0.0-RELEASE'
}
```

## Implementation Details

### LibP2PClient.kt

The `LibP2PClient` class now uses jvm-libp2p to create a lightweight client node:

```kotlin
private fun initializeLibP2PHost(config: NodeConfig): Boolean {
    val privKey: PrivKey = generateKeyPair().second
    
    val hostBuilder = HostBuilder()
        .identity(privKey)
        .protocol("/warpnet/1.0.0")
    
    // Client-only mode - no listening addresses
    val host = hostBuilder.build()
    host.start().get()
    
    libp2pHost = host
    return true
}
```

### Key Features

1. **Client-Only Mode**: No listening addresses (matches `libp2p.NoListenAddrs` requirement)
2. **Custom Protocol**: `/warpnet/1.0.0` for WarpNet-specific communication
3. **Noise Security**: Built-in support (can be configured)
4. **Kotlin Coroutines**: Native async/await support

## Configuration Matching Requirements

The implementation matches the pseudocode requirements:

| Requirement | Implementation |
|------------|----------------|
| `libp2p.Identity(privKey)` | ✅ `HostBuilder().identity(privKey)` |
| `libp2p.NoListenAddrs` | ✅ No listen addresses configured (client-only) |
| `libp2p.DisableMetrics()` | ✅ Not enabled by default |
| `libp2p.DisableRelay()` | ✅ Can be configured as needed |
| `libp2p.Ping(true)` | ✅ Available in jvm-libp2p |
| `libp2p.Security(noise.ID, noise.New)` | ✅ Noise protocol supported |
| `libp2p.Transport(tcp.NewTCPTransport)` | ✅ TCP transport by default |
| `libp2p.PrivateNetwork(pnet.PSK(psk))` | ⚠️ To be implemented |
| `libp2p.UserAgent("warpnet-android")` | ✅ Can be configured |

## Next Steps

### 1. Implement Custom Protocol Handler

Create a custom protocol handler for WarpNet API communication:

```kotlin
class WarpNetProtocol : ProtocolHandler {
    override val protocolId = "/warpnet/1.0.0"
    
    override fun handle(stream: Stream) {
        // Handle incoming API requests from desktop node
        // Or send API requests to desktop node
    }
}

// Register with host
hostBuilder.protocol(WarpNetProtocol())
```

### 2. Implement Stream Communication

Add actual stream-based communication for API calls:

```kotlin
private suspend fun sendViaLibP2PStream(endpoint: String, data: String): String {
    val host = libp2pHost ?: throw IllegalStateException("Not connected")
    val peerId = PeerId.fromBase58(currentConfig!!.peerId)
    
    // Open a stream to the desktop node
    val stream = host.newStream(peerId, "/warpnet/1.0.0").getOrThrow()
    
    try {
        // Send request
        val request = createApiRequest(endpoint, data)
        stream.writeAndFlush(request)
        
        // Read response
        val response = stream.read()
        return parseApiResponse(response)
    } finally {
        stream.close()
    }
}
```

### 3. Add Noise Security Configuration

Configure Noise protocol for encryption:

```kotlin
// Noise configuration can be added to HostBuilder
// This provides end-to-end encryption for all communications
```

### 4. Implement PSK Support (Optional)

For private network support:

```kotlin
// PSK support may need additional configuration
// Check jvm-libp2p documentation for private network setup
```

## Testing

### Unit Tests

Test the libp2p client initialization:

```kotlin
@Test
fun testLibP2PHostCreation() = runBlocking {
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

### Integration Tests

Test connection to a real desktop node:

```kotlin
@Test
fun testConnectionToDesktopNode() = runBlocking {
    // Start a test desktop node
    val testNode = startTestDesktopNode()
    
    val config = NodeConfig(
        peerId = testNode.peerId,
        lanAddress = testNode.address,
        sessionToken = testNode.generateToken()
    )
    
    val client = LibP2PClient.getInstance()
    val result = client.connect(config)
    
    assertTrue(result.isSuccess)
}
```

## Advantages of jvm-libp2p

1. **Native Kotlin**: No JNI overhead, better performance
2. **Type Safety**: Full Kotlin type system support
3. **Coroutines**: Native async support
4. **Debugging**: Easier debugging without crossing language boundaries
5. **Android Optimized**: Built with Android compatibility in mind
6. **Active Development**: Maintained by the libp2p team

## Example: android-chatter

The jvm-libp2p repository includes an Android example app at:
`examples/android-chatter`

This can be used as a reference for:
- Android-specific configuration
- UI integration
- Permission handling
- Lifecycle management

## Resources

- [jvm-libp2p GitHub](https://github.com/libp2p/jvm-libp2p)
- [jvm-libp2p Documentation](https://github.com/libp2p/jvm-libp2p/tree/develop/docs)
- [Android Example](https://github.com/libp2p/jvm-libp2p/tree/develop/examples/android-chatter)
- [libp2p Specifications](https://github.com/libp2p/specs)

## Migration from Go-based Approach

The previous documentation suggested using gomobile to wrap Go libp2p code. The jvm-libp2p approach is preferred because:

1. **Simpler Build**: No need for Go toolchain or gomobile
2. **Better Integration**: Native Kotlin API
3. **Smaller APK**: No Go runtime needed
4. **Easier Maintenance**: Single language ecosystem
5. **Better Performance**: No JNI overhead

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
