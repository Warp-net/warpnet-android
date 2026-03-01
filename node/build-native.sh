#!/bin/bash
# Build script for WarpNet Android native library

set -e

echo "Building WarpNet native library for Android..."

# Check if gomobile is installed
if ! command -v gomobile &> /dev/null; then
    echo "Error: gomobile not found. Install it with:"
    echo "  go install golang.org/x/mobile/cmd/gomobile@latest"
    echo "  go install golang.org/x/mobile/cmd/gobind@latest"
    echo "  gomobile init"
    exit 1
fi


# Add golang.org/x/mobile dependency required by gomobile bind
echo "Adding gomobile dependencies..."
#go get golang.org/x/mobile/bind

# Build for Android

echo "Building Android library..."
gomobile bind -v -androidapi 21 -target=android -o warpnet.aar .
mv warpnet.aar ../android/libs/warpnet.aar
mv warpnet-sources.jar ../android/libs/warpnet-sources.jar

echo "Build complete! Library created at app/libs/warpnet.aar"
