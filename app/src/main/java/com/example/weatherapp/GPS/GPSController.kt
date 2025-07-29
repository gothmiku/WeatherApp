package com.example.weatherapp.GPS

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.weatherapp.MainActivity
import com.google.android.gms.location.LocationServices

class GPSController(context : Context) {


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
}