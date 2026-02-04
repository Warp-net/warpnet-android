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
     * Expected format: JSON with peerId, addresses, sessionToken, and optional PSK
     */
    fun parse(qrContent: String): Result<QRCodeData> {
        return try {
            val json = gson.fromJson(qrContent, JsonObject::class.java)
            
            val peerId = json.get("peerId")?.asString
                ?: return Result.failure(Exception("Missing peerId"))
            
            val addressesJson = json.getAsJsonArray("addresses")
            val addresses = mutableListOf<String>()
            addressesJson?.forEach { addresses.add(it.asString) }
            
            if (addresses.isEmpty()) {
                return Result.failure(Exception("No addresses provided"))
            }
            
            val sessionToken = json.get("sessionToken")?.asString
                ?: return Result.failure(Exception("Missing sessionToken"))
            
            val psk = json.get("psk")?.asString
            
            val qrData = QRCodeData(
                peerId = peerId,
                addresses = addresses,
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
