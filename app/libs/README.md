# WarpNet Native Library Placeholder

This directory will contain the `warpnet.aar` library built from the Go libp2p implementation.

## Building the Native Library

To build the native library, run:

```bash
cd /path/to/warpnet-android
./scripts/build-native.sh
```

This will:
1. Download Go dependencies
2. Build the Go code using gomobile
3. Generate `warpnet.aar` in this directory

## Prerequisites

- Go 1.21 or higher
- gomobile tool (`go install golang.org/x/mobile/cmd/gomobile@latest`)
- Android SDK and NDK

## Note

The `warpnet.aar` file is excluded from git (see `.gitignore`) as it's a build artifact.
Each developer must build it locally before building the Android app.
