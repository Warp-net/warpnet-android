# WarpNet Android Development Guide

This guide helps developers set up and work on the WarpNet Android thin client.

## Project Overview

The WarpNet Android app is designed as a **thin alias client** that:
- Provides a mobile UI for WarpNet
- Delegates all heavy operations to a desktop node
- Uses libp2p for secure communication
- Stores minimal state on the device

## Project Structure

```
warpnet-android/
├── app/
│   ├── build.gradle                    # App-level build configuration
│   ├── src/main/
│   │   ├── AndroidManifest.xml         # App manifest
│   │   ├── java/net/warp/android/
│   │   │   ├── data/                   # Data layer
│   │   │   │   ├── ConfigManager.kt    # Configuration persistence
│   │   │   │   └── Models.kt           # Data models
│   │   │   ├── network/                # Network layer
│   │   │   │   ├── LibP2PClient.kt     # libp2p connection manager
│   │   │   │   └── NodeApiClient.kt    # Desktop node API client
│   │   │   ├── ui/                     # User interface
│   │   │   │   ├── MainActivity.kt     # Main screen
│   │   │   │   ├── QRScanActivity.kt   # QR code scanner
│   │   │   │   └── SettingsActivity.kt # Settings screen
│   │   │   └── util/                   # Utilities
│   │   │       └── QRCodeParser.kt     # QR code parsing
│   │   └── res/                        # Android resources
│   │       ├── layout/                 # XML layouts
│   │       ├── values/                 # Strings, colors, themes
│   │       ├── drawable/               # Graphics
│   │       └── mipmap/                 # App icons
│   └── proguard-rules.pro             # ProGuard configuration
├── build.gradle                        # Project-level build config
├── settings.gradle                     # Gradle settings
├── gradle.properties                   # Gradle properties
├── README.md                          # Project README
├── LIBP2P_INTEGRATION.md              # libp2p integration guide
└── DESKTOP_API_SPEC.md                # Desktop API specification
```

## Code Architecture

### Data Layer

**ConfigManager**: Manages app configuration using SharedPreferences
- Only persistent data in the thin client
- Stores node connection details
- Minimal state by design

**Models**: Data classes
- `NodeConfig`: Connection configuration
- `ConnectionStatus`: Connection state enum
- `QRCodeData`: Parsed QR code information

### Network Layer

**LibP2PClient**: Manages libp2p connection
- Singleton instance
- Connection lifecycle management
- Status notifications via listener pattern
- **Note**: Currently placeholder implementation; needs native Go integration

**NodeApiClient**: API client for desktop node
- RESTful-style API over libp2p
- JSON serialization with Gson
- Coroutine-based async operations

### UI Layer

**MainActivity**: Main application screen
- Connection status display
- Feed view
- Quick actions (notifications, messages)
- Post creation

**QRScanActivity**: QR code scanning
- Uses ZXing library
- Parses desktop node QR codes
- Saves configuration automatically

**SettingsActivity**: Manual configuration
- Node connection details
- LAN/Remote/Relay addresses
- Configuration management

## Development Setup

### Prerequisites

1. **Java Development Kit (JDK)**
   - Version 11 or higher
   - Recommend OpenJDK 17

2. **Android SDK** (if using Android Studio)
   - API Level 24 minimum
   - API Level 34 for compilation

3. **Gradle**
   - Version 8.1.1 (via wrapper)

### IDE Setup

#### Android Studio (Recommended)

1. Download [Android Studio](https://developer.android.com/studio)
2. Open the project: `File > Open > warpnet-android/`
3. Sync Gradle: `File > Sync Project with Gradle Files`
4. Wait for dependencies to download

#### IntelliJ IDEA

1. Download [IntelliJ IDEA](https://www.jetbrains.com/idea/)
2. Install Android plugin
3. Open the project
4. Configure Android SDK in Project Structure

#### VS Code

1. Install Java Extension Pack
2. Install Kotlin extension
3. Install Gradle extension
4. Open project folder

## Building

### Command Line Build

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK (requires signing configuration)
./gradlew assembleRelease

# Clean build
./gradlew clean

# Run tests
./gradlew test
```

Output APK location:
- Debug: `app/build/outputs/apk/debug/app-debug.apk`
- Release: `app/build/outputs/apk/release/app-release.apk`

### Android Studio Build

1. Click **Build > Build Bundle(s) / APK(s) > Build APK(s)**
2. Wait for build to complete
3. Click "locate" in the notification to find the APK

## Running

### On Emulator

1. Create an AVD (Android Virtual Device):
   - Android Studio: `Tools > Device Manager > Create Device`
   - Choose a device (e.g., Pixel 5)
   - Select Android 11 or higher

2. Run the app:
   ```bash
   ./gradlew installDebug
   ```
   Or use the "Run" button in Android Studio

### On Physical Device

1. Enable Developer Options on your Android device:
   - Go to `Settings > About Phone`
   - Tap "Build Number" 7 times

2. Enable USB Debugging:
   - Go to `Settings > Developer Options`
   - Enable "USB Debugging"

3. Connect device via USB

4. Verify connection:
   ```bash
   adb devices
   ```

5. Install and run:
   ```bash
   ./gradlew installDebug
   ```

## Testing

### Unit Tests

Located in `app/src/test/`

Run with:
```bash
./gradlew test
```

### Instrumented Tests

Located in `app/src/androidTest/`

Run with:
```bash
./gradlew connectedAndroidTest
```

Requires a connected device or emulator.

## Code Style

### Kotlin Style

- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use 4 spaces for indentation
- Maximum line length: 120 characters

### Android Conventions

- Activity names: `<Purpose>Activity.kt`
- ViewModels: `<Purpose>ViewModel.kt`
- Fragments: `<Purpose>Fragment.kt`
- Use AndroidX libraries

### Formatting

Format code in Android Studio: `Ctrl+Alt+L` (Windows/Linux) or `Cmd+Option+L` (Mac)

## Debugging

### ADB Commands

```bash
# View logs
adb logcat | grep WarpNet

# Clear app data
adb shell pm clear net.warp.android

# Uninstall app
adb uninstall net.warp.android

# Install APK
adb install -r app/build/outputs/apk/debug/app-debug.apk

# Screen capture
adb exec-out screencap -p > screenshot.png
```

### Debugging in Android Studio

1. Set breakpoints in code
2. Click "Debug" (Shift+F9)
3. Use Debug panel to inspect variables

### Logging

Use Android's Log class:
```kotlin
import android.util.Log

private val TAG = "MyClass"

Log.d(TAG, "Debug message")
Log.i(TAG, "Info message")
Log.w(TAG, "Warning message")
Log.e(TAG, "Error message", exception)
```

View logs with Logcat in Android Studio or:
```bash
adb logcat -s WarpNet:D
```

## Common Issues

### Gradle Sync Failed

**Problem**: Gradle sync fails with dependency errors

**Solution**:
1. Check internet connection
2. Run `./gradlew --refresh-dependencies`
3. Invalidate caches: `File > Invalidate Caches / Restart`

### Build Fails with "SDK not found"

**Problem**: Android SDK not configured

**Solution**:
1. Install Android SDK via Android Studio
2. Set `ANDROID_HOME` environment variable
3. Add to `local.properties`:
   ```
   sdk.dir=/path/to/android/sdk
   ```

### App Crashes on Launch

**Problem**: App crashes immediately

**Solutions**:
1. Check logcat for stack trace
2. Verify manifest permissions
3. Ensure minimum SDK version compatibility
4. Check for missing resources

### Camera Not Working in Emulator

**Problem**: QR scanner doesn't work

**Solution**:
- Use a physical device for camera features
- Or configure emulator camera settings
- Or use manual configuration instead

## Contributing

### Workflow

1. Create a feature branch:
   ```bash
   git checkout -b feature/my-feature
   ```

2. Make changes and commit:
   ```bash
   git add .
   git commit -m "Add my feature"
   ```

3. Push and create PR:
   ```bash
   git push origin feature/my-feature
   ```

### Commit Messages

Format: `<type>: <description>`

Types:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation
- `style`: Code style changes
- `refactor`: Code refactoring
- `test`: Tests
- `chore`: Build/tooling

Examples:
```
feat: Add QR code scanning
fix: Fix connection timeout issue
docs: Update README with build instructions
```

## Next Steps for Development

### Priority Tasks

1. **Implement Native libp2p Integration**
   - See `LIBP2P_INTEGRATION.md`
   - Create Go module with gomobile
   - Build native libraries for Android

2. **Desktop Node Integration**
   - Implement API endpoints on desktop node
   - See `DESKTOP_API_SPEC.md`
   - Add QR code generation

3. **Enhanced UI**
   - Add pull-to-refresh
   - Implement pagination
   - Add image attachments
   - Improve error handling

4. **Testing**
   - Write unit tests
   - Add integration tests
   - Test with real desktop node

5. **Security**
   - Implement secure storage (EncryptedSharedPreferences)
   - Add certificate pinning
   - Token refresh mechanism

## Resources

### Documentation

- [Android Developer Docs](https://developer.android.com/)
- [Kotlin Documentation](https://kotlinlang.org/docs/)
- [libp2p Documentation](https://docs.libp2p.io/)
- [Material Design](https://material.io/design)

### Libraries

- [AndroidX](https://developer.android.com/jetpack/androidx)
- [ZXing](https://github.com/zxing/zxing)
- [OkHttp](https://square.github.io/okhttp/)
- [Gson](https://github.com/google/gson)

### Tools

- [Android Studio](https://developer.android.com/studio)
- [scrcpy](https://github.com/Genymobile/scrcpy) - Screen mirroring
- [Vysor](https://www.vysor.io/) - Device mirroring

## Getting Help

- Check existing issues on GitHub
- Read the documentation in this repository
- Ask questions in discussions
- Contact the WarpNet team

## License

[License information to be added]
