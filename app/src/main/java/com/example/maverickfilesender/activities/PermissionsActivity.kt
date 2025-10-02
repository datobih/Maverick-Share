package com.example.maverickfilesender.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.maverickfilesender.R
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.databinding.ActivityPermissionsBinding

class PermissionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPermissionsBinding
    
    // Modern permission launcher
    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            binding.btnEnablePermission.isEnabled = true
            
            val allGranted = permissions.entries.all { it.value }
            if (allGranted) {
                // Check if we need MANAGE_EXTERNAL_STORAGE for Android 11+
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (!Environment.isExternalStorageManager()) {
                        requestAllFilesAccess()
                    } else {
                        finishWithSuccess()
                    }
                } else {
                    finishWithSuccess()
                }
            } else {
                // Some permissions denied - button is re-enabled, user can retry
            }
        }
    
    // Launcher for MANAGE_EXTERNAL_STORAGE special permission
    private val manageStorageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            binding.btnEnablePermission.isEnabled = true
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    finishWithSuccess()
                } else {
                    // User didn't grant - button is enabled for retry
                }
            } else {
                finishWithSuccess()
            }
        }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_MaverickFileSender)
        binding = ActivityPermissionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val buttonAnimation = AnimationUtils.loadAnimation(this, R.anim.spawn_enable)
        binding.btnEnablePermission.startAnimation(buttonAnimation)

        // Check if all permissions are already granted
        if (hasAllRequiredPermissions()) {
            finishWithSuccess()
            return
        }

        binding.btnEnablePermission.setOnClickListener {
            binding.btnEnablePermission.isEnabled = false
            checkAndRequestPermissions()
        }
    }
    
    private fun hasAllRequiredPermissions(): Boolean {
        val storagePermissionsGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ - Check media permissions
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
        } else {
            // Android 12 and below - Check READ_EXTERNAL_STORAGE
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
        
        // Check MANAGE_EXTERNAL_STORAGE for Android 11+
        val manageStorageGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            true // Not needed for Android 10 and below
        }
        
        return storagePermissionsGranted && manageStorageGranted
    }
    
    private fun checkAndRequestPermissions() {
        val permissionsToRequest = mutableListOf<String>()

        // 1. Handle Storage Permissions based on Android version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ - Request granular media permissions
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_MEDIA_VIDEO)
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_MEDIA_AUDIO)
            }
        } else {
            // Android 12 and below - Request READ_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }

        // 2. Handle Nearby Devices / Location Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ - Request NEARBY_WIFI_DEVICES
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.NEARBY_WIFI_DEVICES) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.NEARBY_WIFI_DEVICES)
            }
        } else {
            // Android 12 and below - Need location for Wi-Fi
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }

        // Request the permissions
        if (permissionsToRequest.isNotEmpty()) {
            // Check if we should show rationale (user previously denied)
            val shouldShowRationale = permissionsToRequest.any { permission ->
                ActivityCompat.shouldShowRequestPermissionRationale(this, permission)
            }
            
            if (shouldShowRationale) {
                // User denied before, send to settings
                openAppSettings()
            } else {
                // First time or "Don't ask again" not checked - request normally
                requestMultiplePermissions.launch(permissionsToRequest.toTypedArray())
            }
        } else {
            // Standard permissions granted, check MANAGE_EXTERNAL_STORAGE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
                requestAllFilesAccess()
            } else {
                finishWithSuccess()
            }
        }
    }
    
    private fun requestAllFilesAccess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = Uri.parse("package:${applicationContext.packageName}")
                manageStorageLauncher.launch(intent)
            } catch (e: Exception) {
                // Fallback for devices that might not have this screen
                try {
                    val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                    manageStorageLauncher.launch(intent)
                } catch (ex: Exception) {
                    // If both fail, just finish
                    binding.btnEnablePermission.isEnabled = true
                }
            }
        }
    }
    
    private fun openAppSettings() {
        binding.btnEnablePermission.isEnabled = true
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }
    
    private fun finishWithSuccess() {
        setResult(Activity.RESULT_OK)
        overridePendingTransition(0, 0)
        finish()
    }

    override fun onResume() {
        super.onResume()
        // Re-enable button when user returns from settings
        binding.btnEnablePermission.isEnabled = true
        
        // Auto-finish if all permissions are now granted
        if (hasAllRequiredPermissions()) {
            finishWithSuccess()
        }
    }
}