package net.warp.android.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * API client for communicating with the desktop WarpNet node
 * All operations are delegated to the desktop node
 */
class NodeApiClient(private val libp2pClient: LibP2PClient) {
    private val TAG = "NodeApiClient"
    private val gson = Gson()
    
    /**
     * Get feed from desktop node
     */
    suspend fun getFeed(limit: Int = 20): Result<List<FeedItem>> = withContext(Dispatchers.IO) {
        try {
            val request = JsonObject().apply {
                addProperty("limit", limit)
            }
            
            val result = libp2pClient.sendMessage("/warpnet.timeline/1.0.0", gson.toJson(request))
            result.fold(
                onSuccess = { response ->
                    // Parse response
                    val items = parseFeedResponse(response)
                    Result.success(items)
                },
                onFailure = { e ->
                    Result.failure(e)
                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error getting feed", e)
            Result.failure(e)
        }
    }
    
    /**
     * Post a new message to the network via desktop node
     */
    suspend fun createPost(content: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val request = JsonObject().apply {
                addProperty("content", content)
            }
            
            val result = libp2pClient.sendMessage("/warpnet.timeline/1.0.0", gson.toJson(request))
            result.fold(
                onSuccess = { Result.success(true) },
                onFailure = { e -> Result.failure(e) }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error creating post", e)
            Result.failure(e)
        }
    }
    
    /**
     * Get notifications from desktop node
     */
    suspend fun getNotifications(): Result<List<Notification>> = withContext(Dispatchers.IO) {
        try {
            val result = libp2pClient.sendMessage("/warpnet.notifications/1.0.0", "{}")
            result.fold(
                onSuccess = { response ->
                    val notifications = parseNotificationsResponse(response)
                    Result.success(notifications)
                },
                onFailure = { e -> Result.failure(e) }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error getting notifications", e)
            Result.failure(e)
        }
    }
    
    /**
     * Get messages from desktop node
     */
    suspend fun getMessages(): Result<List<Message>> = withContext(Dispatchers.IO) {
        try {
            val result = libp2pClient.sendMessage("/warpnet.messages/1.0.0", "{}")
            result.fold(
                onSuccess = { response ->
                    val messages = parseMessagesResponse(response)
                    Result.success(messages)
                },
                onFailure = { e -> Result.failure(e) }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error getting messages", e)
            Result.failure(e)
        }
    }
    
    private fun parseFeedResponse(json: String): List<FeedItem> {
        return try {
            val jsonObject = gson.fromJson(json, com.google.gson.JsonObject::class.java)
            val items = jsonObject.getAsJsonArray("items") ?: return emptyList()
            
            items.map { element ->
                val item = element.asJsonObject
                FeedItem(
                    id = item.get("id")?.asString ?: "",
                    author = item.get("author")?.asString ?: "",
                    content = item.get("content")?.asString ?: "",
                    timestamp = item.get("timestamp")?.asLong ?: 0L
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing feed response", e)
            emptyList()
        }
    }
    
    private fun parseNotificationsResponse(json: String): List<Notification> {
        return try {
            val jsonObject = gson.fromJson(json, com.google.gson.JsonObject::class.java)
            val items = jsonObject.getAsJsonArray("notifications") ?: return emptyList()
            
            items.map { element ->
                val item = element.asJsonObject
                Notification(
                    id = item.get("id")?.asString ?: "",
                    type = item.get("type")?.asString ?: "",
                    content = item.get("content")?.asString ?: "",
                    timestamp = item.get("timestamp")?.asLong ?: 0L
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing notifications response", e)
            emptyList()
        }
    }
    
    private fun parseMessagesResponse(json: String): List<Message> {
        return try {
            val jsonObject = gson.fromJson(json, com.google.gson.JsonObject::class.java)
            val items = jsonObject.getAsJsonArray("messages") ?: return emptyList()
            
            items.map { element ->
                val item = element.asJsonObject
                Message(
                    id = item.get("id")?.asString ?: "",
                    from = item.get("from")?.asString ?: "",
                    content = item.get("content")?.asString ?: "",
                    timestamp = item.get("timestamp")?.asLong ?: 0L
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing messages response", e)
            emptyList()
        }
    }
    
    data class FeedItem(
        val id: String,
        val author: String,
        val content: String,
        val timestamp: Long
    )
    
    data class Notification(
        val id: String,
        val type: String,
        val content: String,
        val timestamp: Long
    )
    
    data class Message(
        val id: String,
        val from: String,
        val content: String,
        val timestamp: Long
    )
}
