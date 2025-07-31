package com.example.weatherapp.data.repo

import com.example.weatherapp.data.local.AppDatabase
import com.example.weatherapp.data.model.WeatherInfo
import com.example.weatherapp.data.local.WeatherInfoDAO
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class WeatherRepo @Inject constructor(private val dao: WeatherInfoDAO) {
    private val weatherInfoDAO: WeatherInfoDAO = dao

    val allWeatherInfos: Flow<List<WeatherInfo>> = weatherInfoDAO.getAllWeatherInfoFlow()



    suspend fun insertWeatherInfo(weatherInfo : WeatherInfo) {
        weatherInfoDAO.insertWeatherInfo(weatherInfo)
    }

    suspend fun getAllWeatherInfo(): List<WeatherInfo> {
        return weatherInfoDAO.getAllWeatherInfo()
    }

    suspend fun getLatestWeatherInfo(): WeatherInfo? {
        return weatherInfoDAO.getLatestWeatherInfo()
    }

    suspend fun deleteOldestWeatherInfo(){
        weatherInfoDAO.deleteOldestWeatherInfo()
    }

    suspend fun deleteAllWeatherInfo(){
        weatherInfoDAO.deleteAllWeatherInfo()
    }


}