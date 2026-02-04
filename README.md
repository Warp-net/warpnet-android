# WarpNet Android - Thin Alias Client

Incomplete

## Prerequisites

- Android 7.0 (API 24) or higher
- A running WarpNet desktop node
- Network connectivity (WiFi or mobile data)

## Building

### Requirements

- JDK 17 or higher
- Gradle 8.1.0

### Build Steps

1. Clone the repository:
```bash
git clone https://github.com/Warp-net/warpnet-android.git
cd warpnet-android
```

2. Build the APK:
```bash
./gradlew assembleDebug
```

The APK will be generated at: `app/build/outputs/apk/debug/app-debug.apk`

### Building Release APK

```bash
./gradlew assembleRelease
```


