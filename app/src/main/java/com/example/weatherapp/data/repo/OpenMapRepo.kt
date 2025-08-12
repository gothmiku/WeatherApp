package com.example.weatherapp.data.repo

import android.util.Log
import com.example.weatherapp.data.model.AddressResponse
import com.example.weatherapp.data.remote.OpenMapAPI
import javax.inject.Inject

class OpenMapRepo @Inject constructor(private val api: OpenMapAPI) {
    suspend fun getJSON(lat : Float, lon : Float) : AddressResponse{
        val response = api.getJSON(lat,lon)
        if (response.isSuccessful) {
            return response.body()!!
        }else{
            Log.e("WeatherRepo", "Error Code: ${response.code()}")
            throw Exception("Error fetching today's weather")
        }
    }
}