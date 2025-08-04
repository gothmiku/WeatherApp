package com.example.weatherapp.data.remote

import android.util.Log
import com.example.weatherapp.BuildConfig.API_KEY
import com.example.weatherapp.data.model.WeatherResponse
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset


interface WeatherAPI{

    @GET("onecall")
    suspend fun getToday(
        @Query("lat") latitude : Float,
        @Query("lon") longtitude : Float,
        @Query("exclude") exclude : String = "minutely,hourly,alerts",
        @Query("appid") apiKey : String = API_KEY,
        @Query("units") units : String = "metric",
        @Query("dt") date : String = LocalDate.now(ZoneId.systemDefault()).atStartOfDay(ZoneId.systemDefault()).toEpochSecond().toString()
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

    @GET("onecall")
    suspend fun getForecast2(
        @Query("lat") latitude : Float,
        @Query("lon") longtitude : Float,
        @Query("exclude") exclude : String = "minutely,current,alerts",
        @Query("appid") apiKey : String = API_KEY,
        @Query("units") units : String = "metric",
        @Query("date") date : String = LocalDate.now().plusDays(2).toString()


    ) : Response<WeatherResponse>

    @GET("onecall")
    suspend fun getForecast3(
        @Query("lat") latitude : Float,
        @Query("lon") longtitude : Float,
        @Query("exclude") exclude : String = "minutely,current,alerts",
        @Query("appid") apiKey : String = API_KEY,
        @Query("units") units : String = "metric",
        @Query("date") date : String = LocalDate.now().plusDays(3).toString()


    ) : Response<WeatherResponse>


    fun <T> retrofitErrorHandler(res: Response<T>): T {
        if (res.isSuccessful) {
            return res.body()!!
        } else {
            val errMsg = res.errorBody()?.string()?.let {
                JSONObject(it).getString("error") // or whatever your message is
            } ?: run {
                res.code().toString()
            }

            throw Exception(errMsg)
        }
    }

    fun checkDateEquals(date: String, response: WeatherResponse): Boolean {
        if(response.dt == date){
            Log.d("Time","Entered date $date and response date ${response.dt} are equals")
            return true
        }else{
            Log.d("Time","Entered date $date and response date ${response.dt} are not equals")
            return false
        }
    }

}