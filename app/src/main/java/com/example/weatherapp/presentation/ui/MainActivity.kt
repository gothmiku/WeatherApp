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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherInfo
import com.example.weatherapp.data.remote.GPSController
import com.example.weatherapp.presentation.viewmodel.WeatherAppViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var weatherViewModel: WeatherAppViewModel
    private lateinit var gpsController: GPSController

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize ViewModel and GPS Controller OUTSIDE the listener
        weatherViewModel = ViewModelProvider(this)[WeatherAppViewModel::class.java]
        gpsController = GPSController(this)

        // Set up window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Log API key
        val apiKey = BuildConfig.API_KEY
        Log.d("API", "API KEY IS $apiKey")
        Log.d("API_KEY", "API Key value: ${BuildConfig.API_KEY}")

        // Check and request location permission
        checkLocationPermission()

        // Insert sample weather data
        insertSampleWeatherData()
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission already granted, get location
            gpsController.getLastLocation()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get location
                gpsController.getLastLocation()
            } else {
                Log.d("GPS", "Location permission denied")
            }
        }
    }

    private fun insertSampleWeatherData() {
        lifecycleScope.launch {
            try {
                val weatherInfo = WeatherInfo(
                    date = LocalDate.now().toString(),
                    temperature = "100",
                    humidity = "30",
                    windSpeed = "10",
                    pressure = "10"
                )
                weatherViewModel.insertWeatherInfo(weatherInfo)
                Log.d("Database", "Weather info inserted successfully")
            } catch (e: Exception) {
                Log.e("Database", "Error inserting weather info", e)
            }
        }
    }
}