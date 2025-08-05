package com.example.weatherapp.data.repo

import android.util.Log
import com.example.weatherapp.data.local.WeatherInfoDAO
import com.example.weatherapp.data.model.WeatherInfo
import com.example.weatherapp.data.model.WeatherResponse
import com.example.weatherapp.data.remote.WeatherAPI
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class WeatherRepo @Inject constructor(private val dao: WeatherInfoDAO, private val api: WeatherAPI) {

    // Remove this line - it's causing the crash
    // val weatherInfo = dao.getLatestWeatherInfo()

    val allWeatherInfos: Flow<List<WeatherInfo>> = dao.getAllWeatherInfoFlow()

    suspend fun getTodayWeather(latitude: Float, longitude: Float): WeatherResponse{
        val response = api.getToday(latitude, longitude)
        if (response.isSuccessful) {
            return response.body()!!
        }else{
            Log.e("WeatherRepo", "Error Code: ${response.code()}")
            throw Exception("Error fetching today's weather")
        }
    }

    suspend fun getForecast(latitude: Float, longitude: Float): WeatherResponse{
        val response = api.getForecast(latitude, longitude)
        if (response.isSuccessful) {
            return response.body()!!
        }else{
            Log.e("WeatherRepo", "Error Code: ${response.code()}")
            throw Exception("Error fetching forecast")
        }
    }

    fun convertWeatherResponseToWeatherInfo(response: WeatherResponse): WeatherInfo {
        return WeatherInfo(
            temp=response.data[0].temp,
            feels_like=response.data[0].feelsLike,
            pressure=response.data[0].pressure,
            humidity=response.data[0].humidity,
            uvi=response.data[0].uvi,
            clouds=response.data[0].clouds,
            visibility=response.data[0].visibility,
            wind_speed=response.data[0].windSpeed,
            date=response.data[0].dt.toString()
        )
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

    suspend fun getLatestWeatherInfo(): WeatherInfo? {
        return dao.getLatestWeatherInfo()
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