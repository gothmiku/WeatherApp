package com.example.weatherapp.data.remote

import com.example.weatherapp.data.model.AddressResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMapAPI {
        @GET("reverse?format=json")
        suspend fun getJSON(
            @Query("lat")lat: Float,
            @Query("lon")lon: Float
        ) : Response<AddressResponse>


}