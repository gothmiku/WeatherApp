package com.example.weatherapp.data.repo

import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.weatherapp.data.local.WeatherInfoDAO
import com.example.weatherapp.data.model.Forecast
import com.example.weatherapp.data.model.WeatherInfo
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.remote.WeatherAPI
import com.example.weatherapp.presentation.ui.date
import com.example.weatherapp.presentation.viewmodel.GPSViewModel
import com.example.weatherapp.presentation.viewmodel.WeatherAppViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class WeatherRepo @Inject constructor(private val dao: WeatherInfoDAO, private val api: WeatherAPI) {

    // Remove this line - it's causing the crash
    // val weatherInfo = dao.getLatestWeatherInfo()

    val allWeatherInfos: Flow<List<WeatherInfo>> = dao.getAllWeatherInfoFlow()

    fun haversine(lat : Float, lon : Float, secondLat : Float, secondLon : Float): Double {
        val earthRadiusKm: Double = 6372.8
        val dLat = Math.toRadians((secondLat - lat).toDouble());
        val dLon = Math.toRadians((secondLon - lon).toDouble());
        val originLat = Math.toRadians(lat.toDouble());
        val destinationLat = Math.toRadians(secondLat.toDouble());

        val a = Math.pow(Math.sin(dLat / 2), 2.toDouble()) + Math.pow(Math.sin(dLon / 2), 2.toDouble()) * Math.cos(originLat) * Math.cos(destinationLat);
        val c = 2 * Math.asin(Math.sqrt(a));
        return earthRadiusKm * c;
    }

    suspend fun getTodayWeather(latitude: Float, longitude: Float): WeatherResponse{
        val response = api.getToday(latitude, longitude)
        if (response.isSuccessful) {
            return response.body()!!
        }else{
            Log.e("WeatherRepo", "Error Code: ${response.code()}")
            throw Exception("Error fetching today's weather")
        }
    }

    suspend fun getForecastWeather(latitude: Float, longitude: Float): Forecast {
        val response = api.getForecast(latitude, longitude)
        if (response.isSuccessful) {
            return response.body()!!
        }else{
            Log.e("WeatherRepo", "Error Code: ${response.code()}")
            throw Exception("Error fetching forecast")
        }
    }

    /**
     * Converts forecast response to WeatherInfo.
     *
     * @param response The forecast response object.
     * @param dayFromNow The number of days from now to get the forecast for.
     *  */
    fun convertForecastResponseToWeatherInfo(response: Forecast,dayFromNow : Int): WeatherInfo {
        return WeatherInfo(
            temp=response.daily[dayFromNow].temp.day,
            feels_like=response.daily[dayFromNow].feelsLike.day,
            pressure=response.daily[dayFromNow].pressure,
            humidity=response.daily[dayFromNow].humidity,
            uvi=response.daily[dayFromNow].uvi,
            clouds=response.daily[dayFromNow].clouds,
            visibility=response.daily[dayFromNow].visibility,
            wind_speed=response.daily[dayFromNow].windSpeed,
            wind_deg = response.daily[dayFromNow].windDeg,
            date=response.daily[dayFromNow].dt.toString(),
            weather = response.daily[dayFromNow].weather[0].main,
            weatherDescription = response.daily[dayFromNow].weather[0].description
        )
    }

    fun convertWeatherResponseToWeatherInfo(response: WeatherResponse): WeatherInfo {
        return WeatherInfo(
            temp=response.current.temp,
            feels_like=response.current.feelsLike,
            pressure=response.current.pressure,
            humidity=response.current.humidity,
            uvi=response.current.uvi,
            clouds=response.current.clouds,
            visibility=response.current.visibility,
            wind_speed=response.current.windSpeed,
            wind_deg = response.current.windDeg,
            date=response.current.dt.toString(),
            weather = response.current.weather[0].main,
            weatherDescription = response.current.weather[0].description
        )
    }

    @RequiresPermission(allOf = [android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION])
    fun fillEmptyDayFields(weatherViewModel: WeatherAppViewModel,gpsViewModel: GPSViewModel) {
        val listToKeep = mutableListOf<String>()
        CoroutineScope(Dispatchers.IO).launch {
            val coordinates = gpsViewModel.getLastLocation()
            if (coordinates == null) {
                Log.e("GPS", "Failed to get location - location is null")
                return@launch
            }

            gpsViewModel.insertGPSInfo(coordinates)
            val gpsInfo = gpsViewModel.getLatestsGPSInfo()

            if(weatherViewModel.getOldestWeatherInfo()?.date == date.getDate().toString()){ //It checks if there are any irrelevant rows and deletes them. It then inserts the latest day
                weatherViewModel.deleteOldestWeatherInfo()
                val weather = weatherViewModel.getForecastWeather(gpsInfo!!.lat, gpsInfo.lon)
                val input = weatherViewModel.convertForecastResponseToWeatherInfo(weather,7)
                weatherViewModel.insertWeatherInfo(input)
            }else{
                Log.d("Database","Day is up to date")
            }
            Log.d("Database","Logging all the dates in the database...")
            for (x in 0..7) {
                val dateString = date.getDatePlusDay(x.toLong()).toString()
                if (weatherViewModel.getWeatherInfoByDate(dateString) != null) {
                    listToKeep.add(dateString) //Making a list of dates that already exist in the database and is within the first 7 days (8 If you count today)
                }
            }
            Log.d("Database","Deleting all the dates that are not in the date list...")
            weatherViewModel.deleteAllExcept(listToKeep) // Deletes everything that is not in the date list
            Log.d("Database","Deleted successfully")
            if(weatherViewModel.getWeatherInfoCount()<=8 || haversine(coordinates.lat,coordinates.lon,gpsInfo!!.lat,gpsInfo.lon)>80){
                val weather = weatherViewModel.getForecastWeather(gpsInfo!!.lat, gpsInfo.lon)
                Log.d("API", "API call successful")
                Log.d("API","Response is:\n${weather.toString()}")
                for(x in 0..7){
                    val expectedDate = date.getDatePlusDay(x.toLong()).toString()
                    if (weatherViewModel.getWeatherInfoByDate(expectedDate) == null) {
                        val input = weatherViewModel.convertForecastResponseToWeatherInfo(weather, x)
                        Log.d("Insertion", "Input data is the class of ${input.javaClass}" +
                                "\n and the data date is ${input.date}" +
                                "\n and the data temp is ${input.temp}")
                        weatherViewModel.insertWeatherInfo(input)
                    }
                }
            }else{
                Log.d("Database","No missing entries.Database is up to date")
            }
        }
    }



    fun <T> retrofitErrorHandler(res: Response<T>): T {
        return api.retrofitErrorHandler(res)
    }

    fun checkDateEquals(date: String, response: WeatherResponse): Boolean {
        return api.checkDateEquals(date, response)
    }

    suspend fun insertWeatherInfo(weatherInfo : WeatherInfo) {
        dao.insertWeatherInfo(weatherInfo)
    }

    suspend fun getAllWeatherInfo(): List<WeatherInfo> {
        return dao.getAllWeatherInfo()
    }

    suspend fun deleteAllExcept(datesToKeep: List<String>) {
        dao.deleteAllExcept(datesToKeep)
    }

    suspend fun getLatestWeatherInfo(): WeatherInfo? {
        return dao.getLatestWeatherInfo()
    }

    suspend fun getOldestWeatherInfo(): WeatherInfo? {
        return dao.getOldestWeatherInfo()
    }

    suspend fun deleteOldestWeatherInfo(){
        dao.deleteOldestWeatherInfo()
    }

    suspend fun deleteAllWeatherInfo(){
        dao.deleteAllWeatherInfo()
    }

    suspend fun getWeatherInfoCount(): Int {
        return dao.getWeatherInfoCount()
    }

    suspend fun getWeatherInfoByDate(date: String): WeatherInfo? {
        return dao.getWeatherInfoByDate(date)
    }

    suspend fun update(weatherInfo: WeatherInfo){
        return dao.update(weatherInfo)
    }
}