# WarpNet Android - Build & Testing Guide

## Quick Start

This guide explains how to build and test the WarpNet Android thin client.

## Prerequisites

### Required Tools

1. **Go 1.21+**
   ```bash
   # Check version
   go version
   ```

2. **gomobile**
   ```bash
   # Install
   go install golang.org/x/mobile/cmd/gomobile@latest
   
   # Initialize (first time only)
   gomobile init
   ```

3. **Android SDK & NDK**
   - Install via Android Studio
   - Set `ANDROID_HOME` environment variable

4. **JDK 17+**
   ```bash
   # Check version
   java -version
   ```

## Build Process

### Step 1: Build Native Library

```bash
# Make script executable (first time only)
chmod +x scripts/build-native.sh

# Build the native Go libp2p library
./scripts/build-native.sh
```

This will:
- Download Go dependencies
- Build Go code using gomobile
- Generate `app/libs/warpnet.aar`

**Troubleshooting**:
- If gomobile is not found: Install with `go install golang.org/x/mobile/cmd/gomobile@latest`
- If NDK is missing: Install via Android Studio SDK Manager
- If build fails: Check Go version is 1.21+

### Step 2: Build Android APK

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/debug/app-debug.apk`

### Step 3: Install on Device

```bash
# Install via ADB
adb install app/build/outputs/apk/debug/app-debug.apk

# Or open in Android Studio and run
```

## Testing

### 1. UI Testing (No Desktop Node Required)

You can test the UI without a desktop node:

1. Launch the app
2. Navigate through screens:
   - Main screen (shows "Not Connected")
   - QR scanner (camera permission required)
   - Settings screen (manual configuration)

### 2. QR Code Testing

Generate a test QR code:

```bash
# Use the provided Python script
python3 scripts/generate_test_qr.py
```

Scan with the app to test QR parsing.

### 3. Connection Testing (Desktop Node Required)

To test actual connection, you need a WarpNet desktop node with:

1. **libp2p listening** on a port
2. **Mobile API endpoints** implemented
3. **QR code generation** with:
   - Peer ID
   - Multiaddr (LAN/remote)
   - Session token
   - Optional PSK

**Test Flow**:
1. Start desktop node
2. Generate QR code from desktop
3. Scan with Android app
4. Verify connection status
5. Test API calls (feed, messages, etc.)

## Architecture Overview

```
┌─────────────────────────────┐
│  Android App (Kotlin)       │
│  - MainActivity             │
│  - QRScanActivity          │
│  - SettingsActivity        │
└──────────┬──────────────────┘
           │ JNI
┌──────────┴──────────────────┐
│  Native Library (.aar)      │
│  - Go libp2p client         │
│  - Noise encryption         │
│  - PSK support              │
└──────────┬──────────────────┘
           │ libp2p streams
┌──────────┴──────────────────┐
│  Desktop WarpNet Node       │
│  - API endpoints            │
│  - Data storage             │
│  - Federation               │
└─────────────────────────────┘
```

## Development Workflow

### Making Changes

1. **Go code changes** (`native/warpnet-client/`):
   ```bash
   # Edit Go files
   # Rebuild native library
   ./scripts/build-native.sh
   # Rebuild Android app
   ./gradlew assembleDebug
   ```

2. **Kotlin code changes** (`app/src/main/java/`):
   ```bash
   # Edit Kotlin files
   # Rebuild Android app (no need to rebuild native lib)
   ./gradlew assembleDebug
   ```

3. **UI changes** (`app/src/main/res/`):
   ```bash
   # Edit XML layouts
   # Rebuild Android app
   ./gradlew assembleDebug
   ```

### Testing Changes

```bash
# Build and install
./scripts/build-native.sh && ./gradlew assembleDebug && adb install -r app/build/outputs/apk/debug/app-debug.apk

# View logs
adb logcat -s LibP2PClient:* MainActivity:*
```

## Configuration

### Test Configuration

Create a test config in Settings:

- **Peer ID**: `12D3KooWTest123...` (from desktop node)
- **LAN Address**: `/ip4/192.168.1.100/tcp/4001`
- **Session Token**: Base64-encoded token from desktop
- **PSK**: (Optional) 32-byte key for private network

### QR Code Format

Desktop node should generate QR with JSON:

```json
{
  "peerId": "12D3KooW...",
  "lanAddress": "/ip4/192.168.1.100/tcp/4001",
  "remoteAddress": "/ip4/1.2.3.4/tcp/4001",
  "relayAddress": "/p2p-circuit/...",
  "sessionToken": "base64-token",
  "psk": "base64-psk",
  "expiresAt": "2026-02-12T00:00:00Z"
}
```

## API Protocol

The app communicates via libp2p streams using these protocols:

- `/warpnet/api/feed/1.0.0` - Get feed
- `/warpnet/api/post/1.0.0` - Create post
- `/warpnet/api/notifications/1.0.0` - Get notifications
- `/warpnet/api/messages/1.0.0` - Get/send messages
- `/warpnet/api/profile/1.0.0` - Get profile

**Request Format**: JSON string
**Response Format**: JSON string

See `DESKTOP_API_SPEC.md` for full API details.

## Troubleshooting

### Build Issues

**Problem**: `gomobile not found`
```bash
go install golang.org/x/mobile/cmd/gomobile@latest
gomobile init
```

**Problem**: `ANDROID_HOME not set`
```bash
export ANDROID_HOME=/path/to/Android/Sdk
```

**Problem**: `Go version too old`
```bash
# Upgrade to Go 1.21+
# Visit https://golang.org/dl/
```

### Runtime Issues

**Problem**: `UnsatisfiedLinkError: libgojni.so`
- Rebuild native library: `./scripts/build-native.sh`
- Clean and rebuild: `./gradlew clean assembleDebug`
- Check AAR contains .so files for your device architecture

**Problem**: `Connection failed`
- Verify desktop node is running
- Check network connectivity
- Verify peer ID and address are correct
- Check firewall settings

**Problem**: `Session token invalid`
- Generate new token from desktop node
- Check token hasn't expired
- Verify token format (base64)

### Debugging

**Enable verbose logging**:
```bash
adb logcat -s LibP2PClient:V MainActivity:V NodeApiClient:V
```

**Check native library**:
```bash
unzip -l app/libs/warpnet.aar | grep .so
```

**Verify connection**:
```bash
# On desktop node, check for incoming connection
# Look for Android client peer ID in logs
```

## Performance

### APK Size

- Without native lib: ~5 MB
- With native lib: ~15-20 MB (includes Go runtime)
- Release build with ProGuard: ~12-15 MB

### Memory Usage

- Idle: ~50 MB
- Active (streaming): ~80-100 MB
- Peak: ~120 MB

### Network Usage

- Minimal (thin client)
- Only data: API requests/responses
- No local storage
- No background sync (while disconnected)

## Security Notes

### Encryption

All communication encrypted with:
- **Noise protocol** (end-to-end)
- **TLS** (for relay connections)
- **PSK** (optional, for private networks)

### Storage

App stores only:
- Node configuration (peer ID, address)
- Session token (encrypted in SharedPreferences)
- PSK (if used, encrypted)

No user data stored locally.

### Permissions

Required:
- **INTERNET** - Network communication
- **CAMERA** - QR code scanning
- **ACCESS_NETWORK_STATE** - Check connectivity

## Next Steps

1. **Build the native library**
   ```bash
   ./scripts/build-native.sh
   ```

2. **Build the Android app**
   ```bash
   ./gradlew assembleDebug
   ```

3. **Implement desktop node API**
   - See `DESKTOP_API_SPEC.md`

4. **Test end-to-end**
   - Connect app to desktop node
   - Verify all API endpoints
   - Test on different networks (LAN, remote, relay)

## Resources

- **Main docs**: README.md
- **libp2p integration**: LIBP2P_INTEGRATION.md
- **Desktop API spec**: DESKTOP_API_SPEC.md
- **Development guide**: DEVELOPMENT.md
- **Example configs**: EXAMPLE_CONFIG.md

## Support

For issues:
- Check troubleshooting section above
- Review logs: `adb logcat`
- Check documentation
- Open GitHub issue
