package com.example.weatherapp.data.repo

import android.util.Log
import com.example.weatherapp.data.local.WeatherInfoDAO
import com.example.weatherapp.data.model.Forecast
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
            date=response.daily[dayFromNow].dt.toString()
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
            date=response.current.dt.toString()
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