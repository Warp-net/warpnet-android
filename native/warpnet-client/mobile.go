package warpnetclient

import (
	"encoding/base64"
	"encoding/json"
	"fmt"
)

// Mobile-friendly wrapper types for gomobile compatibility
// gomobile bind has limitations on complex types

var clientInstance *WarpNetClient

// Initialize creates a new WarpNet client with optional PSK
// pskBase64: base64-encoded PSK (32 bytes), empty string for no PSK
// Returns error message or empty string on success
func Initialize(pskBase64 string) string {
	var psk []byte
	var err error

	if pskBase64 != "" {
		psk, err = base64.StdEncoding.DecodeString(pskBase64)
		if err != nil {
			return fmt.Sprintf("invalid PSK: %v", err)
		}
		if len(psk) != 32 {
			return "PSK must be exactly 32 bytes"
		}
	}

	client, err := NewClient(psk)
	if err != nil {
		return fmt.Sprintf("failed to create client: %v", err)
	}

	clientInstance = client
	return ""
}

// ConnectToNode establishes connection to desktop node
// peerID: base58-encoded peer ID
// address: multiaddr string (e.g., "/ip4/192.168.1.100/tcp/4001")
// Returns error message or empty string on success
func ConnectToNode(peerID string, address string) string {
	if clientInstance == nil {
		return "client not initialized"
	}

	err := clientInstance.Connect(peerID, address)
	if err != nil {
		return fmt.Sprintf("connection failed: %v", err)
	}

	return ""
}

// SendRequest sends a request to the desktop node
// protocolID: the libp2p protocol (e.g., "/warpnet/api/feed/1.0.0")
// dataJSON: JSON-encoded request data
// Returns JSON response or error in format: {"error": "message"} or {"data": "..."}
func SendRequest(protocolID string, dataJSON string) string {
	if clientInstance == nil {
		return `{"error": "client not initialized"}`
	}

	response, err := clientInstance.SendMessage(protocolID, []byte(dataJSON))
	if err != nil {
		errJSON, _ := json.Marshal(map[string]string{"error": err.Error()})
		return string(errJSON)
	}

	// Wrap response in data field
	result := map[string]string{"data": string(response)}
	resultJSON, _ := json.Marshal(result)
	return string(resultJSON)
}

// GetClientPeerID returns the client's own peer ID
func GetClientPeerID() string {
	if clientInstance == nil {
		return ""
	}
	return clientInstance.GetPeerID()
}

// CheckConnection checks if connected to desktop node
// Returns "true" or "false"
func CheckConnection() string {
	if clientInstance == nil {
		return "false"
	}
	if clientInstance.IsConnected() {
		return "true"
	}
	return "false"
}

// DisconnectFromNode disconnects from the desktop node
// Returns error message or empty string on success
func DisconnectFromNode() string {
	if clientInstance == nil {
		return ""
	}

	err := clientInstance.Disconnect()
	if err != nil {
		return fmt.Sprintf("disconnect failed: %v", err)
	}

	return ""
}

// Shutdown closes the entire client
// Returns error message or empty string on success
func Shutdown() string {
	if clientInstance == nil {
		return ""
	}

	err := clientInstance.Close()
	if err != nil {
		return fmt.Sprintf("shutdown failed: %v", err)
	}

	clientInstance = nil
	return ""
}
