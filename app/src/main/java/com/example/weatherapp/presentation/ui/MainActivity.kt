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
import com.example.weatherapp.presentation.ui.fragment.CurrentWeatherFragment
import com.example.weatherapp.presentation.ui.fragment.HumidityFragment
import com.example.weatherapp.presentation.ui.fragment.LocationFragment
import com.example.weatherapp.presentation.ui.fragment.WeatherForecastFragment
import com.example.weatherapp.presentation.viewmodel.GPSViewModel
import com.example.weatherapp.presentation.viewmodel.WeatherAppViewModel
import com.example.weatherapp.util.DateHandle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //TODO Check if internet is available before checking for newer data

    private lateinit var weatherViewModel: WeatherAppViewModel
    private lateinit var gpsViewModel: GPSViewModel

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
            gpsViewModel = ViewModelProvider(this)[GPSViewModel::class.java]
            Log.d("MainAct", "GPSController initialized")
            val todaysDate = DateHandle()


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

            CoroutineScope(Dispatchers.IO).launch {
                if(todaysDate.getUnixTimestampString()==weatherViewModel.getOldestWeatherInfo()?.date){
                    Log.d("Database","Database is up to date")
                }else{
                    weatherViewModel.fillEmptyDayFields(weatherViewModel,gpsViewModel)
                    Log.d("Database","Database is not up to date")
                }
            }


            // Debugging and Test functions

            //logDatabase(weatherViewModel) // To test stuff without api calls
            //apiTest(weatherViewModel,gpsViewModel)
            //apiForecastTest(gpsViewModel,weatherViewModel)
            //checkAndFillDB(weatherViewModel,gpsViewModel)


//            This places the frame layout with the fragment
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.currentWeatherFragment, CurrentWeatherFragment())
                    .commit()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.humidityFragment, HumidityFragment())
                    .commit()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.weatherForecastFragment, WeatherForecastFragment())
                    .commit()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.locationFragment, LocationFragment())
                    .commit()
            }


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
            lifecycleScope.launch(Dispatchers.IO){
                gpsViewModel.getLatestsGPSInfo()
            }
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
                lifecycleScope.launch(Dispatchers.IO){
                    gpsViewModel.getLatestsGPSInfo()
                }
            } else {
                Log.d("GPS", "Location permission denied")
            }
        }
    }
}