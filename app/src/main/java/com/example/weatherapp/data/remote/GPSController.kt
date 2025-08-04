package com.example.weatherapp.data.remote

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.weatherapp.data.local.GPSDAO
import com.example.weatherapp.data.model.Coordinates
import com.example.weatherapp.data.repo.GPSRepo
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class GPSController @Inject constructor(context: Context, private val gpsDAO: GPSDAO) {


    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    suspend fun getLastLocation(): Coordinates? {
        return coroutineScope {
            var response: Coordinates? = null
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    response = Coordinates(
                        date = LocalDate.now(ZoneId.systemDefault()).atStartOfDay(ZoneId.systemDefault()).toEpochSecond().toString(),
                        lat = latitude.toFloat(),
                        lon = longitude.toFloat()
                    )
                    Log.d("GPS", "Location is $latitude and $longitude")
                } else {
                    Log.d("GPS", "GPS couldn't receive the location data")
                }
            }
            response // This will suspend until the listener completes or if it's already completed.
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    suspend fun insertLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                CoroutineScope(Dispatchers.IO).launch {
                    gpsDAO.insertGPSInfo(Coordinates(
                        date=LocalDate.now(ZoneId.systemDefault()).atStartOfDay(ZoneId.systemDefault()).toEpochSecond().toString(),
                        lat=latitude.toFloat()
                        ,lon= longitude.toFloat()))
                }

                Log.d("GPS", "Location inserted to the table ($latitude and $longitude)")
            } else {
                Log.d("GPS", "GPS couldn't receive the location data")
            }
        }
    }
}
