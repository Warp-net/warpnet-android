package net.warp.android.network

import android.util.Log
import kotlinx.coroutines.*
import net.warp.android.data.ConnectionStatus
import net.warp.android.data.NodeConfig
import warpnetclient.Warpnetclient
import org.json.JSONObject
import java.util.concurrent.atomic.AtomicReference
import android.util.Base64

/**
 * Manages libp2p connection to desktop WarpNet node
 * Uses native Go libp2p implementation via JNI (gomobile)
 */
class LibP2PClient {
    private val TAG = "LibP2PClient"
    
    private val connectionStatus = AtomicReference(ConnectionStatus.DISCONNECTED)
    private val listeners = mutableListOf<ConnectionListener>()
    
    private var currentConfig: NodeConfig? = null
    private var isInitialized = false
    
    companion object {
        @Volatile
        private var instance: LibP2PClient? = null
        
        // Load native library
        init {
            try {
                System.loadLibrary("gojni")
                Log.d("LibP2PClient", "Native library loaded successfully")
            } catch (e: UnsatisfiedLinkError) {
                Log.e("LibP2PClient", "Failed to load native library", e)
            }
        }
        
        fun getInstance(): LibP2PClient {
            return instance ?: synchronized(this) {
                instance ?: LibP2PClient().also { instance = it }
            }
        }
    }
    
    /**
     * Connect to the desktop node using the provided configuration
     */
    suspend fun connect(config: NodeConfig): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Connecting to node: ${config.peerId}")
            updateStatus(ConnectionStatus.CONNECTING)
            
            currentConfig = config
            
            // Initialize native client if not already initialized
            if (!isInitialized) {
                // Encode PSK as base64 without newlines for native layer compatibility
                val pskBase64 = config.psk?.let { 
                    Base64.encodeToString(it, Base64.NO_WRAP)
                } ?: ""
                
                val initError = Warpnetclient.initialize(pskBase64)
                if (initError.isNotEmpty()) {
                    updateStatus(ConnectionStatus.ERROR)
                    return@withContext Result.failure(Exception("Initialization failed: $initError"))
                }
                isInitialized = true
                Log.d(TAG, "Native client initialized")
            }
            
            // Determine which address to use
            val address = when {
                config.useRelay && config.relayAddress != null -> config.relayAddress
                config.lanAddress != null -> config.lanAddress
                config.remoteAddress != null -> config.remoteAddress
                else -> {
                    updateStatus(ConnectionStatus.ERROR)
                    return@withContext Result.failure(Exception("No valid address available"))
                }
            }
            
            // Connect to desktop node
            val connectError = Warpnetclient.connectToNode(config.peerId, address)
            if (connectError.isNotEmpty()) {
                updateStatus(ConnectionStatus.ERROR)
                return@withContext Result.failure(Exception("Connection failed: $connectError"))
            }
            
            updateStatus(ConnectionStatus.CONNECTED)
            Log.d(TAG, "Connected successfully to ${config.peerId}")
            Result.success(Unit)
            
        } catch (e: Exception) {
            Log.e(TAG, "Connection error", e)
            updateStatus(ConnectionStatus.ERROR)
            Result.failure(e)
        }
    }
    
    /**
     * Disconnect from the desktop node
     */
    fun disconnect() {
        Log.d(TAG, "Disconnecting")
        
        try {
            if (isInitialized) {
                val error = Warpnetclient.disconnectFromNode()
                if (error.isNotEmpty()) {
                    Log.w(TAG, "Disconnect error: $error")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error during disconnect", e)
        }
        
        updateStatus(ConnectionStatus.DISCONNECTED)
        currentConfig = null
    }
    
    /**
     * Shutdown the client completely
     */
    fun shutdown() {
        Log.d(TAG, "Shutting down")
        disconnect()
        
        try {
            if (isInitialized) {
                val error = Warpnetclient.shutdown()
                if (error.isNotEmpty()) {
                    Log.w(TAG, "Shutdown error: $error")
                }
                isInitialized = false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error during shutdown", e)
        }
    }
    
    /**
     * Send a message to the desktop node
     */
    suspend fun sendMessage(endpoint: String, data: String): Result<String> = withContext(Dispatchers.IO) {
        if (connectionStatus.get() != ConnectionStatus.CONNECTED) {
            return@withContext Result.failure(Exception("Not connected"))
        }
        
        if (!isInitialized) {
            return@withContext Result.failure(Exception("Client not initialized"))
        }
        
        try {
            // Send request via native client
            val responseJSON = Warpnetclient.sendRequest(endpoint, data)
            
            // Parse response
            val json = JSONObject(responseJSON)
            
            if (json.has("error")) {
                val error = json.getString("error")
                return@withContext Result.failure(Exception(error))
            }
            
            if (json.has("data")) {
                val responseData = json.getString("data")
                return@withContext Result.success(responseData)
            }
            
            return@withContext Result.failure(Exception("Invalid response format"))
            
        } catch (e: Exception) {
            Log.e(TAG, "Send error", e)
            return@withContext Result.failure(e)
        }
    }
    
    fun getStatus(): ConnectionStatus {
        return connectionStatus.get()
    }
    
    fun addConnectionListener(listener: ConnectionListener) {
        listeners.add(listener)
    }
    
    fun removeConnectionListener(listener: ConnectionListener) {
        listeners.remove(listener)
    }
    
    private fun updateStatus(status: ConnectionStatus) {
        connectionStatus.set(status)
        listeners.forEach { it.onStatusChanged(status) }
    }
    
    /**
     * Get the client's peer ID
     */
    fun getClientPeerID(): String {
        return try {
            if (isInitialized) {
                Warpnetclient.getClientPeerID()
            } else {
                ""
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting peer ID", e)
            ""
        }
    }
    
    /**
     * Check connection status via native client
     */
    fun checkNativeConnection(): Boolean {
        return try {
            if (isInitialized) {
                Warpnetclient.checkConnection() == "true"
            } else {
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking connection", e)
            false
        }
    }
    
    interface ConnectionListener {
        fun onStatusChanged(status: ConnectionStatus)
    }
}
