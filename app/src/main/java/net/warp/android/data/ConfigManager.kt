package net.warp.android.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

/**
 * Manages storage of node configuration using SharedPreferences
 * This is the only persistent state in the thin client
 */
class ConfigManager(context: Context) {
    private val prefs: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveConfig(config: NodeConfig) {
        val json = gson.toJson(config)
        prefs.edit().putString(KEY_NODE_CONFIG, json).apply()
    }

    fun getConfig(): NodeConfig? {
        val json = prefs.getString(KEY_NODE_CONFIG, null) ?: return null
        return try {
            gson.fromJson(json, NodeConfig::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun clearConfig() {
        prefs.edit().remove(KEY_NODE_CONFIG).apply()
    }

    fun hasConfig(): Boolean {
        return prefs.contains(KEY_NODE_CONFIG)
    }

    companion object {
        private const val PREFS_NAME = "warpnet_prefs"
        private const val KEY_NODE_CONFIG = "node_config"
    }
}
