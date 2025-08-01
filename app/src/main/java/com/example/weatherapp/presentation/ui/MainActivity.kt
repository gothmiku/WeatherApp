package com.example.weatherapp.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
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
import java.time.LocalDateTime
import com.example.weatherapp.data.model.Weather
import kotlinx.coroutines.Dispatchers

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
        try {
            // Initialize ViewModel and GPS Controller OUTSIDE the listener
            weatherViewModel = ViewModelProvider(this)[WeatherAppViewModel::class.java]
            Log.d("MainAct", "Viewmodel initialized")
            gpsController = GPSController(this)
            Log.d("MainAct", "GPSController initialized")


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
            logDatabase()
        }catch(e:Exception){
            Log.e("MainAct","Error is on onCreate",e)
        }
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

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
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

    private fun logDatabase(){
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                Log.d("Database", "=== DATABASE TEST START ===")

                weatherViewModel.deleteAllWeatherInfo()
                Log.d("Database", "Cleared existing data")

                // Check count after clearing
                val countAfterClear = weatherViewModel.getWeatherInfoCount()
                Log.d("Database", "Count after clear: $countAfterClear")

                // Insert multiple test records
                val testData = listOf(
                    WeatherInfo(
                        date = LocalDateTime.now().toString(),
                        temp = 30.5f,
                        feels_like = 32.0f,
                        humidity = 55,
                        wind_speed = 3.5f,
                        pressure = 1012,
                        clouds = 20,
                        uvi = 7.3f,
                        visibility = 10000,

                    ),
                    WeatherInfo(
                        date = LocalDateTime.now().toString(),
                        temp = 28.0f,
                        feels_like = 29.0f,
                        humidity = 60,
                        wind_speed = 4.2f,
                        pressure = 1010,
                        clouds = 50,
                        uvi = 5.8f,
                        visibility = 9500,

                    ),
                    WeatherInfo(
                        date = LocalDateTime.now().toString(),
                        temp = 25.5f,
                        feels_like = 26.2f,
                        humidity = 70,
                        wind_speed = 5.0f,
                        pressure = 1008,
                        clouds = 90,
                        uvi = 3.5f,
                        visibility = 8000,
                    )
                )

                // Insert each record
                testData.forEach { weather ->
                    Log.d("Database", "Inserting: $weather")
                    weatherViewModel.insertWeatherInfo(weather)
                }

                // Wait a moment for insertions to complete
                kotlinx.coroutines.delay(1000)

                // Check final count
                val finalCount = weatherViewModel.getWeatherInfoCount()
                Log.d("Database", "Final count: $finalCount")

                // Get all data and log it
                val allData = weatherViewModel.getAllWeatherInfo()
                Log.d("Database", "All data retrieved: ${allData.size} items")

                allData.forEachIndexed { index, weather ->
                    Log.d("Database", "Item $index: $weather")
                }

                // Test latest record
                val latest = weatherViewModel.getLatestWeatherInfo()
                Log.d("Database", "Latest record: $latest")

                Log.d("Database", "=== DATABASE TEST END ===")

            } catch (e: Exception) {
                Log.e("Database", "Database test error", e)
            }
        }
    }
}