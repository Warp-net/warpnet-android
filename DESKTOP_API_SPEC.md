# Desktop Node API Specification

This document specifies the API that the desktop WarpNet node must expose for mobile client integration.

## Overview

The desktop node acts as the full WarpNet node, handling all data storage, federation, and heavy computation. The mobile app connects to this API as a thin client.

## Transport

- **Protocol**: libp2p streams over Noise-encrypted channels
- **Encoding**: JSON for request/response bodies
- **Authentication**: Session tokens in request headers

## Authentication

### Session Token Generation

When pairing a mobile client, the desktop node:

1. Generates a cryptographically secure session token
2. Associates it with permissions for the user's account
3. Includes it in the QR code for mobile scanning
4. Validates it on each API request

Example token format:
```
session_<timestamp>_<random_bytes>
```

### Request Authentication

All API requests must include:
```json
{
  "sessionToken": "session_1234567890_abc123def456",
  "endpoint": "/api/feed",
  "data": { ... }
}
```

## API Endpoints

### 1. Get Feed

**Endpoint**: `/api/feed`

**Method**: Request-Response

**Request**:
```json
{
  "sessionToken": "...",
  "limit": 50,
  "offset": 0,
  "before": "timestamp_or_id"
}
```

**Response**:
```json
{
  "status": "ok",
  "items": [
    {
      "id": "post_123",
      "author": {
        "peerId": "12D3KooW...",
        "name": "Alice",
        "avatar": "base64_or_url"
      },
      "content": "Hello WarpNet!",
      "timestamp": 1676543210,
      "likes": 5,
      "replies": 2,
      "attachments": []
    }
  ],
  "hasMore": true
}
```

### 2. Create Post

**Endpoint**: `/api/post`

**Request**:
```json
{
  "sessionToken": "...",
  "content": "My post content",
  "attachments": [],
  "visibility": "public"
}
```

**Response**:
```json
{
  "status": "ok",
  "postId": "post_124",
  "timestamp": 1676543220
}
```

### 3. Get Notifications

**Endpoint**: `/api/notifications`

**Request**:
```json
{
  "sessionToken": "...",
  "unreadOnly": false,
  "limit": 50
}
```

**Response**:
```json
{
  "status": "ok",
  "notifications": [
    {
      "id": "notif_123",
      "type": "like",
      "from": {
        "peerId": "12D3KooW...",
        "name": "Bob"
      },
      "postId": "post_100",
      "timestamp": 1676543200,
      "read": false
    },
    {
      "id": "notif_124",
      "type": "reply",
      "from": {
        "peerId": "12D3KooW...",
        "name": "Carol"
      },
      "postId": "post_101",
      "content": "Great post!",
      "timestamp": 1676543190,
      "read": true
    }
  ]
}
```

### 4. Get Messages

**Endpoint**: `/api/messages`

**Request**:
```json
{
  "sessionToken": "...",
  "conversationId": "optional",
  "limit": 50
}
```

**Response**:
```json
{
  "status": "ok",
  "conversations": [
    {
      "id": "conv_123",
      "peer": {
        "peerId": "12D3KooW...",
        "name": "Dave"
      },
      "lastMessage": {
        "content": "See you later!",
        "timestamp": 1676543180,
        "from": "12D3KooW..."
      },
      "unreadCount": 2
    }
  ],
  "messages": []
}
```

If `conversationId` is provided, returns messages for that conversation:
```json
{
  "status": "ok",
  "messages": [
    {
      "id": "msg_456",
      "from": "12D3KooW...",
      "to": "12D3KooW...",
      "content": "Hello!",
      "timestamp": 1676543100,
      "read": true
    }
  ]
}
```

### 5. Send Message

**Endpoint**: `/api/message/send`

**Request**:
```json
{
  "sessionToken": "...",
  "to": "12D3KooW...",
  "content": "My message"
}
```

**Response**:
```json
{
  "status": "ok",
  "messageId": "msg_457",
  "timestamp": 1676543230
}
```

### 6. Get Profile

**Endpoint**: `/api/profile`

**Request**:
```json
{
  "sessionToken": "...",
  "peerId": "optional_peer_id"
}
```

If no `peerId` is provided, returns the authenticated user's profile.

**Response**:
```json
{
  "status": "ok",
  "profile": {
    "peerId": "12D3KooW...",
    "name": "Alice",
    "bio": "WarpNet enthusiast",
    "avatar": "base64_or_url",
    "followers": 100,
    "following": 50,
    "posts": 250
  }
}
```

### 7. Update Profile

**Endpoint**: `/api/profile/update`

**Request**:
```json
{
  "sessionToken": "...",
  "name": "New Name",
  "bio": "Updated bio",
  "avatar": "base64_encoded_image"
}
```

**Response**:
```json
{
  "status": "ok"
}
```

### 8. Follow/Unfollow

**Endpoint**: `/api/follow`

**Request**:
```json
{
  "sessionToken": "...",
  "peerId": "12D3KooW...",
  "action": "follow"
}
```

`action` can be "follow" or "unfollow".

**Response**:
```json
{
  "status": "ok"
}
```

### 9. Like/Unlike Post

**Endpoint**: `/api/like`

**Request**:
```json
{
  "sessionToken": "...",
  "postId": "post_123",
  "action": "like"
}
```

**Response**:
```json
{
  "status": "ok",
  "likeCount": 6
}
```

### 10. Reply to Post

**Endpoint**: `/api/reply`

**Request**:
```json
{
  "sessionToken": "...",
  "postId": "post_123",
  "content": "My reply"
}
```

**Response**:
```json
{
  "status": "ok",
  "replyId": "post_125"
}
```

### 11. Search

**Endpoint**: `/api/search`

**Request**:
```json
{
  "sessionToken": "...",
  "query": "search terms",
  "type": "posts",
  "limit": 50
}
```

`type` can be: "posts", "people", "all"

**Response**:
```json
{
  "status": "ok",
  "results": {
    "posts": [...],
    "people": [...]
  }
}
```

## Error Responses

All errors follow this format:

```json
{
  "status": "error",
  "error": {
    "code": "INVALID_TOKEN",
    "message": "Session token is invalid or expired"
  }
}
```

### Error Codes

- `INVALID_TOKEN`: Session token is invalid or expired
- `UNAUTHORIZED`: Not authorized to perform this action
- `NOT_FOUND`: Requested resource not found
- `RATE_LIMITED`: Too many requests
- `INTERNAL_ERROR`: Server-side error
- `INVALID_REQUEST`: Malformed request

## Real-time Updates

### WebSocket/Stream Connection

For real-time updates, establish a persistent stream:

**Endpoint**: `/api/stream`

**Request**:
```json
{
  "sessionToken": "...",
  "subscribe": ["feed", "notifications", "messages"]
}
```

**Server Pushes**:
```json
{
  "type": "notification",
  "data": {
    "id": "notif_125",
    "type": "like",
    ...
  }
}
```

## QR Code Format

When pairing a mobile device, the desktop node generates a QR code containing:

```json
{
  "version": "1.0",
  "peerId": "12D3KooWAbCdEf...",
  "addresses": [
    "/ip4/192.168.1.100/tcp/4001",
    "/ip4/203.0.113.50/tcp/4001/p2p/12D3KooW..."
  ],
  "sessionToken": "session_1676543000_xyz789abc",
  "psk": "base64_encoded_psk",
  "nodeInfo": {
    "name": "My WarpNet Node",
    "version": "1.0.0"
  },
  "expires": 1676546600
}
```

The mobile app scans this QR code to obtain all connection details.

## Security Requirements

### Session Management

1. **Token Expiration**: Tokens should expire after 30-90 days
2. **Token Revocation**: Users can revoke mobile client access
3. **Token Refresh**: Implement token refresh mechanism

### Rate Limiting

Implement rate limiting per session token:
- 100 requests per minute for reads
- 20 requests per minute for writes

### Data Validation

- Validate all input data
- Sanitize user-generated content
- Enforce maximum content lengths
- Validate image uploads

### Encryption

- All communication over Noise-encrypted libp2p channels
- Optional PSK for additional network isolation
- End-to-end encryption for direct messages

## Implementation Notes

### Desktop Node (Go)

```go
// Example API handler
func (n *Node) handleFeedRequest(stream network.Stream, req *APIRequest) {
    // Validate session token
    if !n.validateSessionToken(req.SessionToken) {
        sendError(stream, "INVALID_TOKEN", "Invalid session token")
        return
    }
    
    // Get feed data
    feed, err := n.getFeed(req.Data.Limit, req.Data.Offset)
    if err != nil {
        sendError(stream, "INTERNAL_ERROR", err.Error())
        return
    }
    
    // Send response
    sendResponse(stream, map[string]interface{}{
        "status": "ok",
        "items":  feed,
    })
}
```

### Mobile Client (Kotlin)

```kotlin
// Example API call
suspend fun NodeApiClient.getFeed(limit: Int): Result<List<FeedItem>> {
    val request = JsonObject().apply {
        addProperty("sessionToken", sessionToken)
        addProperty("limit", limit)
    }
    
    return libp2pClient.sendMessage("/api/feed", gson.toJson(request))
        .map { response -> parseFeedResponse(response) }
}
```

## Testing

### Test Endpoints

Provide test endpoints for development:

- `/api/test/ping` - Simple connectivity test
- `/api/test/echo` - Echo back request data
- `/api/test/slow` - Simulate slow response

### Mock Data

For development, support mock data mode where the node returns realistic fake data without actual federation.

## Versioning

API version in request:
```json
{
  "apiVersion": "1.0",
  "sessionToken": "...",
  ...
}
```

The node should support multiple API versions for backward compatibility.
