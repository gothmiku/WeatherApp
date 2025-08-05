package com.example.weatherapp.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.local.AppDatabase
import com.example.weatherapp.data.model.Coordinates
import com.example.weatherapp.data.model.WeatherInfo
import com.example.weatherapp.data.repo.GPSRepo
import kotlinx.coroutines.flow.Flow
import com.example.weatherapp.data.repo.WeatherRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class GPSViewModel @Inject constructor(private val repo: GPSRepo) : ViewModel() {


    val allCoordinatesFlow: Flow<List<Coordinates>> = repo.allGPSUpdates

    suspend fun getAllGPSInfo(){
        return repo.getAllGPSInfo()
    }

    suspend fun getGPSInfoByDate(date: String){
        return repo.getGPSInfoByDate(date)
    }

    suspend fun insertGPSInfo(coordinates: Coordinates) {
        repo.insertGPSInfo(coordinates)
    }

    suspend fun getLatestsGPSInfo() : Coordinates?{
        return repo.latestGPSInfo()
    }

    @RequiresPermission(allOf = [android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION])
    suspend fun getLastLocation() : Coordinates?{
        return repo.getLastLocation()
    }


}