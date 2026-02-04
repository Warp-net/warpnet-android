package net.warp.android.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import net.warp.android.R
import net.warp.android.data.ConfigManager
import net.warp.android.data.NodeConfig
import net.warp.android.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var configManager: ConfigManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        configManager = ConfigManager(this)
        
        loadCurrentConfig()
        setupListeners()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
    
    private fun loadCurrentConfig() {
        val config = configManager.getConfig()
        if (config != null) {
            binding.editPeerId.setText(config.peerId)
            binding.editLanAddress.setText(config.lanAddress ?: "")
            binding.editRemoteAddress.setText(config.remoteAddress ?: "")
            binding.switchUseRelay.isChecked = config.useRelay
        }
    }
    
    private fun setupListeners() {
        binding.btnSave.setOnClickListener {
            saveConfiguration()
        }
        
        binding.btnClear.setOnClickListener {
            clearConfiguration()
        }
        
        binding.btnLogout.setOnClickListener {
            logout()
        }
    }
    
    private fun saveConfiguration() {
        val peerId = binding.editPeerId.text.toString().trim()
        
        if (peerId.isEmpty()) {
            Toast.makeText(this, "Peer ID is required", Toast.LENGTH_SHORT).show()
            return
        }
        
        val config = NodeConfig(
            peerId = peerId,
            lanAddress = binding.editLanAddress.text.toString().trim().takeIf { it.isNotEmpty() },
            remoteAddress = binding.editRemoteAddress.text.toString().trim().takeIf { it.isNotEmpty() },
            sessionToken = "manual-" + System.currentTimeMillis(), // Placeholder
            useRelay = binding.switchUseRelay.isChecked
        )
        
        configManager.saveConfig(config)
        Toast.makeText(this, "Configuration saved", Toast.LENGTH_SHORT).show()
        finish()
    }
    
    private fun clearConfiguration() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Clear Settings")
            .setMessage("Are you sure you want to clear all settings?")
            .setPositiveButton("Clear") { _, _ ->
                configManager.clearConfig()
                binding.editPeerId.text?.clear()
                binding.editLanAddress.text?.clear()
                binding.editRemoteAddress.text?.clear()
                binding.switchUseRelay.isChecked = false
                Toast.makeText(this, "Settings cleared", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun logout() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.logout)
            .setMessage("This will disconnect from the node and clear all settings.")
            .setPositiveButton(R.string.logout) { _, _ ->
                configManager.clearConfig()
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
