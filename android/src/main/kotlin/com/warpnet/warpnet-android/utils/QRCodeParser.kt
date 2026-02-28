package net.warp.android.util

import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import net.warp.android.data.QRCodeData

/**
 * Utility class for parsing QR codes from desktop WarpNet node
 */
object QRCodeParser {
    private val TAG = "QRCodeParser"
    private val gson = Gson()
    
    /**
     * Parse QR code data from desktop node
     * Expected format: JSON with peerId, sessionToken, and optional PSK
     */
    fun parse(qrContent: String): Result<QRCodeData> {
        return try {
            val json = gson.fromJson(qrContent, JsonObject::class.java)
            
            // Parse peer ID (may be in "peerId" or "PeerId" field)
            val peerId = json.get("peerId")?.asString ?: json.get("PeerId")?.asString
                ?: return Result.failure(Exception("Missing peerId"))
            
            // Parse session token (may be in "sessionToken" or "SessionToken" field)
            val sessionToken = json.get("sessionToken")?.asString ?: json.get("SessionToken")?.asString
                ?: return Result.failure(Exception("Missing sessionToken"))
            
            // Parse optional PSK (may be in "psk" or "PSK" field)
            val psk = json.get("psk")?.asString ?: json.get("PSK")?.asString
            
            val qrData = QRCodeData(
                peerId = peerId,
                addresses = emptyList(), // Not needed with bootstrap nodes
                sessionToken = sessionToken,
                psk = psk
            )
            
            Log.d(TAG, "Parsed QR code successfully: peerId=$peerId")
            Result.success(qrData)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse QR code", e)
            Result.failure(Exception("Invalid QR code format: ${e.message}"))
        }
    }
    
    /**
     * Decode base64 PSK if present
     */
    fun decodePSK(pskBase64: String?): ByteArray? {
        if (pskBase64 == null) return null
        return try {
            Base64.decode(pskBase64, Base64.DEFAULT)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to decode PSK", e)
            null
        }
    }
}
