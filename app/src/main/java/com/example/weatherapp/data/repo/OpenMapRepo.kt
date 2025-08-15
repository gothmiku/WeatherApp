package com.example.weatherapp.data.repo

import android.util.Log
import com.example.weatherapp.data.model.Address
import com.example.weatherapp.data.model.AddressResponse
import com.example.weatherapp.data.remote.OpenMapAPI
import retrofit2.Response
import javax.inject.Inject

class OpenMapRepo @Inject constructor(private val api: OpenMapAPI) {

    val noResponseAddress = Address("API Error", "API Error", "Default")
    val noResponse = AddressResponse(noResponseAddress)

    suspend fun getJSON(lat : Float, lon : Float) : AddressResponse{
        val response = api.getJSON(lat,lon)
        if (response.isSuccessful) {
            return response.body()!!
        }else{
            Log.e("WeatherRepo", "Error Code: ${response.code()}")
            return noResponse
        }
    }

    suspend fun getJSONResponse(lat : Float, lon : Float) : Response<AddressResponse>{
        val response = api.getJSON(lat,lon)
        if (response.isSuccessful) {
            return response
        }else{
            Log.e("WeatherRepo", "Error Code: ${response.code()}")
            throw Exception("Error fetching location info")
        }
    }
}