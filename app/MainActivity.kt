package com.example.aimbot

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    
    private lateinit var wifiManager: WifiManager
    private var adbIp: String = ""
    private var adbPort: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStartOverlay = findViewById<Button>(R.id.btnStartOverlay)
        val btnConnectAdb = findViewById<Button>(R.idbtnConnectAdb)
        val btnInject = findViewById<Button>(R.id.btnInject)

        wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager

        btnStartOverlay.setOnClickListener {
            if (Settings.canDrawOverlays(this)) {
                startOverlayService()
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, 
                    android.net.Uri.parse("package:$packageName"))
                startActivityForResult(intent, 1001)
            }
        }

        btnConnectAdb.setOnClickListener {
            val ip = wifiManager.connectionInfo?.wifiIpAddress
            if (ip != null) {
                adbIp = ip
                // Puerto estándar para depuración inalámbrica en dispositivos modernos
                adbPort = 5555 
                Toast.makeText(this, "Conectado a ADB: $adbIp", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No se pudo obtener IP", Toast.LENGTH_SHORT).show()
            }
        }

        btnInject.setOnClickListener {
            // Simulación de inyección de aimbot vía ADB
            AdbInjector.executeCommand(adbIp, adbPort, "input tap 500 500")
            Toast.makeText(this, "Inyectando...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startOverlayService() {
        val intent = Intent(this, OverlayService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startOverlayService()
            }
        }
    }
}
