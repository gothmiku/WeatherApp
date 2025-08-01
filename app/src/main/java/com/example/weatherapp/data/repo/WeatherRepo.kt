package com.example.weatherapp.data.repo

import com.example.weatherapp.data.local.WeatherInfoDAO
import com.example.weatherapp.data.model.WeatherInfo
import com.example.weatherapp.data.remote.WeatherAPI
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepo @Inject constructor(private val dao: WeatherInfoDAO, private val api: WeatherAPI) {

    // Remove this line - it's causing the crash
    // val weatherInfo = dao.getLatestWeatherInfo()

    val allWeatherInfos: Flow<List<WeatherInfo>> = dao.getAllWeatherInfoFlow()



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