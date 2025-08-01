package com.example.weatherapp.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.local.AppDatabase
import com.example.weatherapp.data.model.WeatherInfo
import kotlinx.coroutines.flow.Flow
import com.example.weatherapp.data.repo.WeatherRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WeatherAppViewModel @Inject constructor(private val repo: WeatherRepo) : ViewModel() {
    val allWeatherInfos: Flow<List<WeatherInfo>> = repo.allWeatherInfos

    fun insertWeatherInfo(weatherInfo: WeatherInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            if (repo.getWeatherInfoByDate(LocalDate.now().toString()) == weatherInfo) {
                Log.d("ViewModel","It already exists")
            }else{
                repo.insertWeatherInfo(weatherInfo)
            }
        }
    }


    //TODO
    fun checkForUpdate(date : String){
        viewModelScope.launch(Dispatchers.IO) {
            val oldestWeatherInfo = repo.getLatestWeatherInfo()
            if(oldestWeatherInfo.date == date){

            }

        }
    }

    suspend fun getAllWeatherInfo(): List<WeatherInfo> {
        return repo.getAllWeatherInfo()
    }

    suspend fun getLatestWeatherInfo(): WeatherInfo? {
        return repo.getLatestWeatherInfo()
    }

    suspend fun deleteAllWeatherInfo() {
        repo.deleteAllWeatherInfo()
    }

    suspend fun getWeatherInfoCount(): Int {
        return repo.getWeatherInfoCount()
    }

    suspend fun update(weatherInfo: WeatherInfo) {
        repo.update(weatherInfo)
    }

}