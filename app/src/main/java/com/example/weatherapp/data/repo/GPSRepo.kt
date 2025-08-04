package com.example.weatherapp.data.repo

import androidx.annotation.RequiresPermission
import androidx.room.Insert
import androidx.room.Query
import com.example.weatherapp.data.local.GPSDAO
import com.example.weatherapp.data.model.Coordinates
import com.example.weatherapp.data.model.WeatherInfo
import com.example.weatherapp.data.remote.GPSController
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GPSRepo @Inject constructor(private val gpsDAO: GPSDAO,private val controller : GPSController){

    val allGPSUpdates: Flow<List<Coordinates>> = gpsDAO.getAllCoordinatesFlow()

    @RequiresPermission(allOf = [android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION])
    suspend fun getLastLocation() : Coordinates?{
        return controller.getLastLocation()
    }

    @RequiresPermission(allOf = [android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION])
    suspend fun insertLocation(){
        controller.insertLocation()
    }


    suspend fun insertGPSInfo(coordinates: Coordinates){
        gpsDAO.insertGPSInfo(coordinates)
    }

    suspend fun latestGPSInfo() : Coordinates?{
        return gpsDAO.latestGPSInfo()
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