package com.example.weatherapp.data.repo

import androidx.room.Insert
import androidx.room.Query
import com.example.weatherapp.data.local.GPSDAO
import com.example.weatherapp.data.model.Coordinates
import com.example.weatherapp.data.model.WeatherInfo
import com.example.weatherapp.data.remote.GPSController
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GPSRepo @Inject constructor(private val gpsDAO: GPSDAO){

    val allGPSUpdates: Flow<List<Coordinates>> = gpsDAO.getAllCoordinatesFlow()

    fun getLastLocation(){
        GPSController.getLastLocation()
    }

    suspend fun insertGPSInfo(coordinates: Coordinates){
        gpsDAO.insertGPSInfo(coordinates)
    }

    suspend fun latestsGPSInfo(){
        gpsDAO.latestsGPSInfo()
    }

    suspend fun deleteAll(){
        gpsDAO.deleteAll()
    }

    suspend fun gpsInfoCount(){
        gpsDAO.gpsInfoCount()
    }


    suspend fun getAllGPSInfo(){
        gpsDAO.getAllGPSInfo()
    }

    suspend fun getGPSInfoByDate(date: String) {
        gpsDAO.getGPSInfoByDate(date)
    }
}