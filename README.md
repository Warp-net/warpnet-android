# WarpNet Android - Thin Alias Client

An Android application that acts as a thin alias client for WarpNet, delegating all heavy logic and data storage to a WarpNet node running on your personal computer.

## Overview

The WarpNet Android app serves as a remote interface to your desktop WarpNet node, allowing you to:

- View feeds, messages, and notifications from your mobile device
- Send posts and interact with the WarpNet network
- Connect securely to your desktop node via libp2p with Noise encryption
- Work seamlessly behind NAT using relay connections

## Features

- **Lightweight Design**: No database or heavy computation on-device
- **QR Code Authentication**: Easy setup by scanning a QR code from your desktop node
- **Secure Communication**: End-to-end encryption using libp2p Noise protocol
- **NAT Traversal**: Connect to your node from anywhere using relay servers
- **Multiple Connection Types**: Support for LAN, remote, and relay connections
- **Real-time Updates**: Stay connected to your desktop node over mobile networks

## Architecture

This app implements a thin client architecture where all heavy computation happens on the desktop node.

### libp2p Client Configuration

The app creates a lightweight libp2p client with:
- No listening addresses (client-only mode)
- Noise security protocol for encryption
- TCP transport
- Private network support with PSK
- Custom user agent: "warpnet-android"

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

## Project Structure

```
warpnet-android/
├── app/
│   ├── src/main/
│   │   ├── java/net/warp/android/
│   │   │   ├── data/           # Data models and configuration
│   │   │   ├── network/        # libp2p client and API
│   │   │   ├── ui/             # Activities and UI components
│   │   │   └── util/           # Utilities (QR parsing, etc.)
│   │   ├── res/                # Resources (layouts, strings, etc.)
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
└── settings.gradle
```

## Security

- **libp2p Noise Protocol**: End-to-end encryption for all communications
- **Private Network**: Optional PSK for additional network isolation
- **No Local Storage**: No messages or personal data stored on device

## Related Projects

- **Desktop Node Backend**: [warpnet](https://github.com/Warp-net/warpnet)
- **Desktop Node Frontend**: [warpnet-frontend](https://github.com/Warp-net/warpnet-frontend)
