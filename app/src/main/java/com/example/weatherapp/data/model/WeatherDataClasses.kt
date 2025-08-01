package com.example.weatherapp.data.model

data class WeatherResponse(
    val current: CurrentWeather,
)

data class CurrentWeather(
    val temp : Float,
    val feels_like : Float,
    val pressure : Int,
    val humidity : Int,
    val dew_point : Float,
    val uvi : Float,
    val clouds : Int,
    val visibility : Int,
    val wind_speed : Float,
    val weather : List<Weather>
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

