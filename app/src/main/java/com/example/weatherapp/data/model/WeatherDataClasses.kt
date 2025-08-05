package com.example.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val lat: Float,
    val lon: Float,
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int,
    val data: WeatherData
)

data class WeatherData(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Float,
    @SerializedName("feels_like")
    val feelsLike: Float,
    val pressure: Int,
    val humidity: Int,
    @SerializedName("dew_point")
    val dewPoint: Float,
    val uvi: Float,
    val clouds: Int,
    val visibility: Int,
    @SerializedName("wind_speed")
    val windSpeed: Float,
    @SerializedName("wind_deg")
    val windDeg: Int,
    @SerializedName("wind_gust")
    val windGust: Float,
    val weather: Weather
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)