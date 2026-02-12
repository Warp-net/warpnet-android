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

# Navigate to the native client directory
cd "$(dirname "$0")/../native/warpnet-client"

# Fetch Go dependencies
echo "Fetching Go dependencies..."
go mod download
go mod tidy

# Add golang.org/x/mobile dependency required by gomobile bind
echo "Adding gomobile dependencies..."
go get golang.org/x/mobile/bind

# Build for Android

echo "Building Android library..."
gomobile bind -v -androidapi 21 -target=android -o warpnet.aar .
mv warpnet.aar ../../app/libs/warpnet.aar
mv warpnet-sources.jar ../../app/libs/warpnet-sources.jar

echo "Build complete! Library created at app/libs/warpnet.aar"
