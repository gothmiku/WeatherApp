package com.example.weatherapp.data.remote

import com.example.weatherapp.BuildConfig.API_KEY
import com.example.weatherapp.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate
import java.time.LocalDateTime


interface WeatherAPI{
    @GET("onecall")
    suspend fun getToday(
        @Query("lat") latitude : Float,
        @Query("lon") longtitude : Float,
        @Query("exclude") exclude : String = "minutely,hourly,alerts",
        @Query("appid") apiKey : String = API_KEY,
        @Query("units") units : String = "metric",
        @Query("date") date : String = LocalDate.now().toString()
    ) : Response<WeatherResponse>

    @GET("onecall")
    suspend fun getForecast(
        @Query("lat") latitude : Float,
        @Query("lon") longtitude : Float,
        @Query("exclude") exclude : String = "minutely,current,alerts",
        @Query("appid") apiKey : String = API_KEY,
        @Query("units") units : String = "metric",
        @Query("date") date : String = LocalDate.now().plusDays(1).toString()


    ) : Response<WeatherResponse>


}