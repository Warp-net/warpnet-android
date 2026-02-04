# Example Node Configuration

This file provides examples of node configurations for testing and development.

## QR Code Format

The desktop WarpNet node generates a QR code containing connection information in JSON format:

### Basic Configuration

```json
{
  "version": "1.0",
  "peerId": "12D3KooWEyJ8VdZjGpPQj1pV3pGFqYLz8FqW5KzmHx9GnHsCz7LM",
  "addresses": [
    "/ip4/192.168.1.100/tcp/4001"
  ],
  "sessionToken": "session_1676543210_abc123def456ghi789jkl",
  "nodeInfo": {
    "name": "Alice's WarpNet Node",
    "version": "1.0.0"
  },
  "expires": 1676629610
}
```

### With PSK (Private Network)

```json
{
  "version": "1.0",
  "peerId": "12D3KooWEyJ8VdZjGpPQj1pV3pGFqYLz8FqW5KzmHx9GnHsCz7LM",
  "addresses": [
    "/ip4/192.168.1.100/tcp/4001",
    "/ip4/203.0.113.50/tcp/4001"
  ],
  "sessionToken": "session_1676543210_xyz789abc123def456ghi",
  "psk": "VGhpc0lzQVRlc3RQU0tGb3JQZW9wbGU=",
  "nodeInfo": {
    "name": "Bob's Private WarpNet Node",
    "version": "1.0.0"
  },
  "expires": 1676629610
}
```

### With Relay

```json
{
  "version": "1.0",
  "peerId": "12D3KooWEyJ8VdZjGpPQj1pV3pGFqYLz8FqW5KzmHx9GnHsCz7LM",
  "addresses": [
    "/ip4/192.168.1.100/tcp/4001",
    "/ip4/203.0.113.50/tcp/4001",
    "/p2p/12D3KooWRelayNode.../p2p-circuit/p2p/12D3KooWEyJ8..."
  ],
  "sessionToken": "session_1676543210_relay123def456ghi789",
  "nodeInfo": {
    "name": "Carol's Mobile-Friendly Node",
    "version": "1.0.0"
  },
  "expires": 1676629610
}
```

## Manual Configuration

For manual configuration in the Settings screen, use these fields:

### Local Network (LAN)

```
Peer ID: 12D3KooWEyJ8VdZjGpPQj1pV3pGFqYLz8FqW5KzmHx9GnHsCz7LM
LAN Address: /ip4/192.168.1.100/tcp/4001
Remote Address: (leave empty)
Use Relay: unchecked
```

### Remote Network

```
Peer ID: 12D3KooWEyJ8VdZjGpPQj1pV3pGFqYLz8FqW5KzmHx9GnHsCz7LM
LAN Address: (leave empty)
Remote Address: /ip4/203.0.113.50/tcp/4001
Use Relay: unchecked
```

### With Relay

```
Peer ID: 12D3KooWEyJ8VdZjGpPQj1pV3pGFqYLz8FqW5KzmHx9GnHsCz7LM
LAN Address: /ip4/192.168.1.100/tcp/4001
Remote Address: /ip4/203.0.113.50/tcp/4001
Use Relay: checked
```

## Desktop Node Setup

To set up a test desktop node for development:

### 1. Install WarpNet Desktop Node

```bash
# Clone the desktop node repository
git clone https://github.com/Warp-net/warpnet.git
cd warpnet

# Build and run
go build -o warpnet ./cmd/warpnet
./warpnet init
./warpnet start
```

### 2. Generate Mobile Client Configuration

The desktop node should expose a command to generate mobile client QR codes:

```bash
# Generate a QR code for mobile pairing
./warpnet mobile-pair --output qr.png
```

This generates:
- A session token
- QR code image
- Configuration JSON

### 3. Configure API Access

Enable the mobile client API in the desktop node configuration:

```yaml
# warpnet.yaml
api:
  enabled: true
  mobile_clients: true
  session_timeout: 2592000  # 30 days
  rate_limit:
    reads_per_minute: 100
    writes_per_minute: 20
```

## Test Data

For development and testing, you can use these mock values:

### Mock Peer IDs

```
12D3KooWEyJ8VdZjGpPQj1pV3pGFqYLz8FqW5KzmHx9GnHsCz7LM
12D3KooWAbCdEfGhIjKlMnOpQrStUvWxYz1234567890AbCdEfGh
12D3KooWTestNode1234567890abcdefghijklmnopqrstuvwxyz
```

### Mock Session Tokens

```
session_1676543210_abc123def456ghi789jkl012mno345pqr678
session_1676543220_xyz789abc123def456ghi789jkl012mno345
session_1676543230_test123mock456session789token012abc345
```

### Mock Addresses

#### LAN Addresses
```
/ip4/192.168.1.100/tcp/4001
/ip4/192.168.1.101/tcp/4001
/ip4/10.0.0.50/tcp/4001
```

#### Public Addresses
```
/ip4/203.0.113.50/tcp/4001
/ip4/198.51.100.25/tcp/4001
/ip4/192.0.2.100/tcp/4001
```

#### Relay Addresses
```
/p2p/12D3KooWRelay123.../p2p-circuit/p2p/12D3KooWEyJ8...
/p2p/12D3KooWRelay456.../p2p-circuit/p2p/12D3KooWAbCd...
```

## API Response Examples

### Feed Response

```json
{
  "status": "ok",
  "items": [
    {
      "id": "post_1676543210_001",
      "author": {
        "peerId": "12D3KooWEyJ8...",
        "name": "Alice",
        "avatar": ""
      },
      "content": "Hello WarpNet from mobile!",
      "timestamp": 1676543210,
      "likes": 5,
      "replies": 2,
      "attachments": []
    },
    {
      "id": "post_1676543180_002",
      "author": {
        "peerId": "12D3KooWAbCd...",
        "name": "Bob",
        "avatar": ""
      },
      "content": "Testing the Android app",
      "timestamp": 1676543180,
      "likes": 3,
      "replies": 1,
      "attachments": []
    }
  ],
  "hasMore": true
}
```

### Notification Response

```json
{
  "status": "ok",
  "notifications": [
    {
      "id": "notif_1676543200_001",
      "type": "like",
      "from": {
        "peerId": "12D3KooWAbCd...",
        "name": "Bob"
      },
      "postId": "post_1676543100_005",
      "timestamp": 1676543200,
      "read": false
    },
    {
      "id": "notif_1676543150_002",
      "type": "reply",
      "from": {
        "peerId": "12D3KooWTest...",
        "name": "Carol"
      },
      "postId": "post_1676543050_003",
      "content": "Great post!",
      "timestamp": 1676543150,
      "read": true
    }
  ]
}
```

## Environment Variables

For testing different configurations:

```bash
# Local development
export WARPNET_NODE_ADDRESS="192.168.1.100:4001"
export WARPNET_USE_RELAY="false"

# Remote testing
export WARPNET_NODE_ADDRESS="203.0.113.50:4001"
export WARPNET_USE_RELAY="true"

# Mock mode (for UI development)
export WARPNET_MOCK_MODE="true"
export WARPNET_MOCK_DELAY="1000"  # milliseconds
```

## Debugging Configuration

### Enable Verbose Logging

In `app/build.gradle`:

```gradle
android {
    buildTypes {
        debug {
            buildConfigField "boolean", "DEBUG_LIBP2P", "true"
            buildConfigField "boolean", "MOCK_API", "true"
        }
    }
}
```

Usage in code:

```kotlin
if (BuildConfig.DEBUG_LIBP2P) {
    Log.d(TAG, "libp2p connection details: $details")
}

if (BuildConfig.MOCK_API) {
    return mockApiResponse()
}
```

## Creating Test QR Codes

You can generate test QR codes using online tools or command line:

### Using qrencode

```bash
# Install qrencode
sudo apt-get install qrencode  # Ubuntu/Debian
brew install qrencode          # macOS

# Generate QR code
echo '{
  "peerId": "12D3KooWEyJ8VdZjGpPQj1pV3pGFqYLz8FqW5KzmHx9GnHsCz7LM",
  "addresses": ["/ip4/192.168.1.100/tcp/4001"],
  "sessionToken": "session_test_123456",
  "nodeInfo": {"name": "Test Node", "version": "1.0.0"}
}' | qrencode -o test-qr.png

# Display QR code in terminal
qrencode -t ANSIUTF8 < config.json
```

### Using Python

```python
import qrcode
import json

config = {
    "peerId": "12D3KooWEyJ8VdZjGpPQj1pV3pGFqYLz8FqW5KzmHx9GnHsCz7LM",
    "addresses": ["/ip4/192.168.1.100/tcp/4001"],
    "sessionToken": "session_test_123456",
    "nodeInfo": {"name": "Test Node", "version": "1.0.0"}
}

qr = qrcode.make(json.dumps(config))
qr.save("test-qr.png")
```

## Notes

- All timestamps are Unix epoch in seconds
- Peer IDs are libp2p peer IDs (base58btc encoded)
- Session tokens should be cryptographically random
- PSK should be 32 bytes (base64 encoded for JSON)
- Addresses follow libp2p multiaddr format
