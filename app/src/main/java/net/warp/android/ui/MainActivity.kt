package net.warp.android.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import net.warp.android.R
import net.warp.android.data.ConfigManager
import net.warp.android.data.ConnectionStatus
import net.warp.android.databinding.ActivityMainBinding
import net.warp.android.network.LibP2PClient
import net.warp.android.network.NodeApiClient

class MainActivity : AppCompatActivity(), LibP2PClient.ConnectionListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var configManager: ConfigManager
    private lateinit var libp2pClient: LibP2PClient
    private lateinit var apiClient: NodeApiClient
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        
        configManager = ConfigManager(this)
        libp2pClient = LibP2PClient.getInstance()
        apiClient = NodeApiClient(libp2pClient)
        
        setupListeners()
        
        // Check if we have a saved configuration
        val config = configManager.getConfig()
        if (config != null) {
            connectToNode()
        } else {
            showNotConnectedView()
        }
    }
    
    override fun onResume() {
        super.onResume()
        libp2pClient.addConnectionListener(this)
        updateConnectionStatus(libp2pClient.getStatus())
    }
    
    override fun onPause() {
        super.onPause()
        libp2pClient.removeConnectionListener(this)
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun setupListeners() {
        binding.btnScanQr.setOnClickListener {
            startActivity(Intent(this, QRScanActivity::class.java))
        }
        
        binding.btnManualConnect.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        
        binding.btnNotifications.setOnClickListener {
            loadNotifications()
        }
        
        binding.btnMessages.setOnClickListener {
            loadMessages()
        }
        
        binding.fabPost.setOnClickListener {
            showPostDialog()
        }
    }
    
    private fun connectToNode() {
        val config = configManager.getConfig() ?: return
        
        lifecycleScope.launch {
            val result = libp2pClient.connect(config)
            result.fold(
                onSuccess = {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, R.string.connected, Toast.LENGTH_SHORT).show()
                        loadFeed()
                    }
                },
                onFailure = { error ->
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.error_connection_failed) + ": ${error.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            )
        }
    }
    
    private fun loadFeed() {
        lifecycleScope.launch {
            val result = apiClient.getFeed()
            result.fold(
                onSuccess = { items ->
                    runOnUiThread {
                        if (items.isEmpty()) {
                            binding.feedContent.text = "No posts yet"
                        } else {
                            binding.feedContent.text = items.joinToString("\n\n") {
                                "${it.author}: ${it.content}"
                            }
                        }
                    }
                },
                onFailure = { error ->
                    runOnUiThread {
                        binding.feedContent.text = "Error loading feed: ${error.message}"
                    }
                }
            )
        }
    }
    
    private fun loadNotifications() {
        lifecycleScope.launch {
            val result = apiClient.getNotifications()
            result.fold(
                onSuccess = { notifications ->
                    runOnUiThread {
                        val message = if (notifications.isEmpty()) {
                            "No notifications"
                        } else {
                            "${notifications.size} notifications"
                        }
                        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                    }
                },
                onFailure = { error ->
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "Error: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
    }
    
    private fun loadMessages() {
        lifecycleScope.launch {
            val result = apiClient.getMessages()
            result.fold(
                onSuccess = { messages ->
                    runOnUiThread {
                        val message = if (messages.isEmpty()) {
                            "No messages"
                        } else {
                            "${messages.size} messages"
                        }
                        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                    }
                },
                onFailure = { error ->
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "Error: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
    }
    
    private fun showPostDialog() {
        val input = android.widget.EditText(this)
        input.hint = "What's on your mind?"
        
        MaterialAlertDialogBuilder(this)
            .setTitle("Create Post")
            .setView(input)
            .setPositiveButton("Post") { _, _ ->
                val content = input.text.toString()
                if (content.isNotBlank()) {
                    createPost(content)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun createPost(content: String) {
        lifecycleScope.launch {
            val result = apiClient.createPost(content)
            result.fold(
                onSuccess = {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Posted!", Toast.LENGTH_SHORT).show()
                        loadFeed()
                    }
                },
                onFailure = { error ->
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            "Error posting: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
    }
    
    override fun onStatusChanged(status: ConnectionStatus) {
        runOnUiThread {
            updateConnectionStatus(status)
        }
    }
    
    private fun updateConnectionStatus(status: ConnectionStatus) {
        when (status) {
            ConnectionStatus.CONNECTED -> {
                binding.statusText.text = getString(R.string.connected)
                binding.statusIndicator.setBackgroundResource(R.color.status_connected)
                showConnectedView()
                val config = configManager.getConfig()
                binding.nodeAddressText.text = config?.peerId ?: ""
            }
            ConnectionStatus.CONNECTING -> {
                binding.statusText.text = getString(R.string.connecting)
                binding.statusIndicator.setBackgroundResource(R.color.status_connecting)
                showNotConnectedView()
            }
            ConnectionStatus.DISCONNECTED -> {
                binding.statusText.text = getString(R.string.disconnected)
                binding.statusIndicator.setBackgroundResource(R.color.status_disconnected)
                showNotConnectedView()
                binding.nodeAddressText.text = ""
            }
            ConnectionStatus.ERROR -> {
                binding.statusText.text = getString(R.string.node_unavailable)
                binding.statusIndicator.setBackgroundResource(R.color.status_disconnected)
                showNotConnectedView()
            }
        }
    }
    
    private fun showConnectedView() {
        binding.notConnectedView.visibility = View.GONE
        binding.connectedView.visibility = View.VISIBLE
        binding.fabPost.show()
    }
    
    private fun showNotConnectedView() {
        binding.notConnectedView.visibility = View.VISIBLE
        binding.connectedView.visibility = View.GONE
        binding.fabPost.hide()
    }
}
