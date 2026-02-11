#!/bin/bash
# Build script for WarpNet Android native library

set -e

echo "Building WarpNet native library for Android..."

# Check if gomobile is installed
if ! command -v gomobile &> /dev/null; then
    echo "Error: gomobile not found. Install it with:"
    echo "  go install golang.org/x/mobile/cmd/gomobile@latest"
    echo "  gomobile init"
    exit 1
fi

# Navigate to the native client directory
cd "$(dirname "$0")/native/warpnet-client"

# Fetch Go dependencies
echo "Fetching Go dependencies..."
go mod download
go mod tidy

# Build for Android
echo "Building Android library..."
gomobile bind -v -target=android -o ../../app/libs/warpnet.aar .

echo "Build complete! Library created at app/libs/warpnet.aar"
