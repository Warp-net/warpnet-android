# WarpNet Android - Thin Alias Client

An Android application that acts as a thin alias client for the WarpNet desktop node, providing a mobile interface without storing or computing data on-device.

## Architecture

This app uses:
- **Native Go libp2p** - Compiled to .aar via gomobile for full compatibility with WarpNet desktop
- **Kotlin UI** - Clean Material Design 3 interface
- **JNI Bridge** - Seamless connection between Go and Kotlin layers
- **Thin Client Design** - No local database, minimal state

## Features

- 📱 **QR Code Pairing** - Scan QR from desktop node to connect
- 🔐 **Secure Communication** - Noise protocol encryption, optional PSK
- 🌐 **Flexible Connectivity** - LAN, remote, or relay connections
- 💬 **Full Social Features** - Feed, messages, notifications, posting
- ⚡ **Lightweight** - No heavy computation or storage on mobile
- 🔄 **Real-time Sync** - Direct libp2p connection to your desktop node

## Prerequisites

### For Users

- Android 7.0 (API 24) or higher
- A running WarpNet desktop node
- Network connectivity (WiFi or mobile data)

### For Developers

- JDK 17 or higher
- Android SDK (API 34)
- Go 1.21 or higher
- gomobile tool
- Gradle 8.1.0

## Building

### Step 1: Build Native Library

First, build the Go libp2p native library:

```bash
# Install gomobile (first time only)
go install golang.org/x/mobile/cmd/gomobile@latest
gomobile init

# Build the native library
chmod +x scripts/build-native.sh
./scripts/build-native.sh
```

This creates `app/libs/warpnet.aar` containing the Go libp2p implementation.

### Step 2: Build Android App

Build the APK:

```bash
./gradlew assembleDebug
```

The APK will be generated at: `app/build/outputs/apk/debug/app-debug.apk`

### Building Release APK

```bash
./gradlew assembleRelease
```

## Installation

Install on your device:

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Usage

1. **Start your WarpNet desktop node**
2. **Generate QR code** from desktop node (containing peer ID, address, session token)
3. **Open WarpNet Android app**
4. **Tap "Scan QR Code"** and scan the QR from desktop
5. **Connected!** - Your feed and data will appear

### Manual Configuration

Alternatively, go to Settings and manually enter:
- Peer ID
- LAN/Remote address
- Session token
- Optional PSK for private networks

## Project Structure

```
warpnet-android/
├── native/warpnet-client/      # Go libp2p implementation
│   ├── client.go               # Core libp2p client
│   ├── mobile.go               # Gomobile JNI wrapper
│   ├── go.mod                  # Go dependencies
│   └── README.md               # Native module docs
├── app/
│   ├── libs/                   # Native library (.aar)
│   ├── src/main/
│   │   ├── java/net/warp/android/
│   │   │   ├── data/           # Models, config
│   │   │   ├── network/        # LibP2P & API clients
│   │   │   ├── ui/             # Activities
│   │   │   └── util/           # Utilities
│   │   ├── res/                # Android resources
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── scripts/
│   └── build-native.sh         # Native build script
└── docs/                       # Documentation

## Technology Stack

### Native Layer (Go)
- **libp2p** - P2P networking
- **Noise Protocol** - End-to-end encryption
- **PSK** - Private network support
- **gomobile** - Android binding

### Android Layer (Kotlin)
- **Material Design 3** - Modern UI components
- **Coroutines** - Asynchronous programming
- **ViewBinding** - Type-safe view access
- **ZXing** - QR code scanning
- **OkHttp** - HTTP client (fallback)
- **Gson** - JSON parsing

## Documentation

- **[README.md](README.md)** - This file
- **[LIBP2P_INTEGRATION.md](LIBP2P_INTEGRATION.md)** - libp2p implementation details
- **[DESKTOP_API_SPEC.md](DESKTOP_API_SPEC.md)** - Desktop node API specification
- **[DEVELOPMENT.md](DEVELOPMENT.md)** - Development guide
- **[CONTRIBUTING.md](CONTRIBUTING.md)** - Contribution guidelines
- **[EXAMPLE_CONFIG.md](EXAMPLE_CONFIG.md)** - Configuration examples

## Development

See [DEVELOPMENT.md](DEVELOPMENT.md) for:
- Development environment setup
- Code structure
- Testing guide
- Debugging tips

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for contribution guidelines.

## Security

- **Noise Protocol**: All libp2p streams are encrypted
- **Session Tokens**: Time-limited authentication
- **PSK Support**: Optional private network isolation
- **No Local Storage**: Minimal attack surface (thin client)
- **Secure QR Pairing**: Safe initial setup

## License

MIT License - See [LICENSE](LICENSE) for details.

## Related Projects

- [WarpNet Desktop Node](https://github.com/Warp-net/warpnet) - Full desktop implementation
- [WarpNet Frontend](https://github.com/Warp-net/warpnet-frontend) - Web frontend

## Support

For issues and questions:
- GitHub Issues: [warpnet-android/issues](https://github.com/Warp-net/warpnet-android/issues)
- Documentation: See docs in this repository

## Roadmap

- [x] Core libp2p client (Go)
- [x] JNI bridge layer
- [x] Kotlin UI implementation
- [x] QR code pairing
- [x] Settings management
- [ ] Desktop node API implementation
- [ ] Comprehensive testing
- [ ] Background service
- [ ] Push notifications
- [ ] Offline mode
- [ ] Multi-account support

