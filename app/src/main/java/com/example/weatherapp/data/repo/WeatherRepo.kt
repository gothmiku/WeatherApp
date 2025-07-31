package com.example.weatherapp.data.repo

import com.example.weatherapp.data.local.WeatherInfoDAO
import com.example.weatherapp.data.model.WeatherInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepo @Inject constructor(private val dao: WeatherInfoDAO) {

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
}