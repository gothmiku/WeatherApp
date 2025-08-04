package com.example.weatherapp.presentation.ui

import android.util.Log
import com.example.weatherapp.data.model.WeatherInfo
import com.example.weatherapp.presentation.viewmodel.WeatherAppViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import com.example.weatherapp.presentation.viewmodel.GPSViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

fun logDatabase(weatherViewModel: WeatherAppViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
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

//TODO The gave me a null. I think the cause is that my API membership is the free plan.
fun apiTest(weatherViewModel: WeatherAppViewModel,gpsViewModel: GPSViewModel){
    CoroutineScope(Dispatchers.IO).launch {
        val gpsInfo = gpsViewModel.getLatestsGPSInfo()

        try {
            val weather = weatherViewModel.getTodayWeather(gpsInfo!!.lat, gpsInfo.lon)
            Log.d("API", "API test success")
            val input = weatherViewModel.convertWeatherResponseToWeatherInfo(weather)
            weatherViewModel.insertWeatherInfo(input)
        }
        catch (e: Exception) {
            Log.e("API", "API test error", e)
        }
    }

}