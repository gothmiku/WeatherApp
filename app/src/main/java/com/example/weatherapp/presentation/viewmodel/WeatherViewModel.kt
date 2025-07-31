package com.example.weatherapp.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.local.AppDatabase
import com.example.weatherapp.data.model.WeatherInfo
import kotlinx.coroutines.flow.Flow
import com.example.weatherapp.data.repo.WeatherRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherAppViewModel @Inject constructor (private val repo : WeatherRepo) : ViewModel() {
    val allWeatherInfos: Flow<List<WeatherInfo>> = repo.allWeatherInfos

    fun insertWeatherInfo(weatherInfo: WeatherInfo) {
        viewModelScope.launch {
            repo.insertWeatherInfo(weatherInfo)
        }
    }

}