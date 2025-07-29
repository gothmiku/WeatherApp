package com.example.weatherapp.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.R
import com.example.weatherapp.data.remote.GPSController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)

            }
            val gpsController = GPSController(this)
            gpsController.getLastLocation()
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val apiKey = BuildConfig.API_KEY;
            Log.d("API","API KEY IS $apiKey")
            Log.d("API_KEY", "API Key value: ${BuildConfig.API_KEY}")
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)



            insets
        }
    }
}