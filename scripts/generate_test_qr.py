#!/usr/bin/env python3
"""
Generate a test QR code for WarpNet Android development

This script creates a QR code containing mock WarpNet node configuration
for testing the Android app without a real desktop node.
"""

import json
import sys
import time
import base64
import os

def generate_test_config(peer_id=None, include_psk=False, use_relay=False):
    """Generate a test configuration for WarpNet mobile client"""
    
    # Default test peer ID
    if not peer_id:
        peer_id = "12D3KooWEyJ8VdZjGpPQj1pV3pGFqYLz8FqW5KzmHx9GnHsCz7LM"
    
    # Generate session token
    session_token = f"session_{int(time.time())}_test{''.join([chr(97 + i % 26) for i in range(20)])}"
    
    # Base addresses
    addresses = [
        "/ip4/192.168.1.100/tcp/4001",
        "/ip4/203.0.113.50/tcp/4001"
    ]
    
    # Add relay address if requested
    if use_relay:
        relay_addr = f"/p2p/12D3KooWRelay123.../p2p-circuit/p2p/{peer_id}"
        addresses.append(relay_addr)
    
    config = {
        "version": "1.0",
        "peerId": peer_id,
        "addresses": addresses,
        "sessionToken": session_token,
        "nodeInfo": {
            "name": "Test WarpNet Node",
            "version": "1.0.0"
        },
        "expires": int(time.time()) + 86400  # 24 hours from now
    }
    
    # Add PSK if requested
    if include_psk:
        # Generate a test PSK (32 random bytes, base64 encoded)
        test_psk = base64.b64encode(b"This is a test PSK for testing!").decode('ascii')
        config["psk"] = test_psk
    
    return config

def main():
    """Main function"""
    print("WarpNet Android Test QR Code Generator")
    print("=" * 50)
    
    # Parse command line arguments
    include_psk = "--psk" in sys.argv
    use_relay = "--relay" in sys.argv
    
    # Generate configuration
    config = generate_test_config(
        include_psk=include_psk,
        use_relay=use_relay
    )
    
    # Convert to JSON
    config_json = json.dumps(config, indent=2)
    
    print("\nGenerated Configuration:")
    print(config_json)
    
    # Try to generate QR code if qrcode library is available
    try:
        import qrcode
        
        # Create QR code
        qr = qrcode.QRCode(
            version=1,
            error_correction=qrcode.constants.ERROR_CORRECT_L,
            box_size=10,
            border=4,
        )
        qr.add_data(config_json)
        qr.make(fit=True)
        
        # Save to file
        img = qr.make_image(fill_color="black", back_color="white")
        output_file = "test-qr-code.png"
        img.save(output_file)
        
        print(f"\n✓ QR code saved to: {output_file}")
        print("\nScan this QR code with the WarpNet Android app to test connection.")
        
        # Also print ASCII QR code if in terminal
        if sys.stdout.isatty():
            print("\nASCII QR Code (for terminal):")
            qr.print_ascii(invert=True)
        
    except ImportError:
        print("\n⚠ qrcode library not found. To generate QR code images:")
        print("  pip install qrcode[pil]")
        print("\nYou can manually create a QR code from the JSON above using:")
        print("  https://www.qr-code-generator.com/")
        
        # Save JSON to file
        json_file = "test-config.json"
        with open(json_file, 'w') as f:
            f.write(config_json)
        print(f"\n✓ Configuration saved to: {json_file}")
    
    print("\nUsage:")
    print("  python3 generate_test_qr.py          # Basic configuration")
    print("  python3 generate_test_qr.py --psk    # With PSK")
    print("  python3 generate_test_qr.py --relay  # With relay address")
    print("  python3 generate_test_qr.py --psk --relay  # Both")

if __name__ == "__main__":
    main()
