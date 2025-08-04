package com.example.weatherapp.data.remote

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.weatherapp.data.local.GPSDAO
import com.example.weatherapp.data.repo.GPSRepo
import com.google.android.gms.location.LocationServices
import javax.inject.Inject

class GPSController @Inject constructor(context : Context, private val gpsDAO: GPSDAO) {


    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getLastLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                Log.d("GPS", "Location is $latitude and $longitude")
            } else {
                Log.d("GPS", "GPS couldn't receive the location data")
            }
        }
    }

    fun insertLocation(){

    }
}