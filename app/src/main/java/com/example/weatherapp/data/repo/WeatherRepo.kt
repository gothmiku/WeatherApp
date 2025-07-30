package com.example.weatherapp.data.repo

import com.example.weatherapp.data.model.WeatherInfo
import com.example.weatherapp.data.local.WeatherInfoDAO
import kotlinx.coroutines.flow.Flow

class WeatherRepo(private val weatherInfoDAO : WeatherInfoDAO) {
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