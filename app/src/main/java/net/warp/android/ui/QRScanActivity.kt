package net.warp.android.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.warp.android.R
import net.warp.android.data.ConfigManager
import net.warp.android.data.NodeConfig
import net.warp.android.databinding.ActivityQrScanBinding
import net.warp.android.util.QRCodeParser

class QRScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQrScanBinding
    private lateinit var configManager: ConfigManager
    
    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult?) {
            if (result == null) return
            
            val qrContent = result.text
            processQRCode(qrContent)
        }
        
        override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
            // Not used
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        configManager = ConfigManager(this)
        
        checkCameraPermission()
    }
    
    override fun onResume() {
        super.onResume()
        if (hasCameraPermission()) {
            binding.barcodeScanner.resume()
        }
    }
    
    override fun onPause() {
        super.onPause()
        binding.barcodeScanner.pause()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
    
    private fun checkCameraPermission() {
        if (hasCameraPermission()) {
            startScanning()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST
            )
        }
    }
    
    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanning()
            } else {
                Toast.makeText(
                    this,
                    "Camera permission required for QR scanning",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }
    
    private fun startScanning() {
        binding.barcodeScanner.decodeContinuous(callback)
    }
    
    private fun processQRCode(qrContent: String) {
        binding.barcodeScanner.pause()
        
        CoroutineScope(Dispatchers.Main).launch {
            val result = QRCodeParser.parse(qrContent)
            
            result.fold(
                onSuccess = { qrData ->
                    // Convert QR data to NodeConfig
                    val config = NodeConfig(
                        peerId = qrData.peerId,
                        lanAddress = qrData.addresses.firstOrNull(),
                        remoteAddress = qrData.addresses.getOrNull(1),
                        relayAddress = qrData.addresses.getOrNull(2),
                        sessionToken = qrData.sessionToken,
                        psk = QRCodeParser.decodePSK(qrData.psk),
                        useRelay = false
                    )
                    
                    // Save configuration
                    configManager.saveConfig(config)
                    
                    Toast.makeText(
                        this@QRScanActivity,
                        "Configuration saved!",
                        Toast.LENGTH_SHORT
                    ).show()
                    
                    // Return to main activity
                    finish()
                },
                onFailure = { error ->
                    Toast.makeText(
                        this@QRScanActivity,
                        getString(R.string.error_invalid_qr) + ": ${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    
                    // Resume scanning
                    binding.barcodeScanner.resume()
                }
            )
        }
    }
    
    companion object {
        private const val CAMERA_PERMISSION_REQUEST = 100
    }
}
