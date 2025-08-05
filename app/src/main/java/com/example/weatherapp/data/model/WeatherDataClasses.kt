package com.example.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val lat: Float,
    val lon: Float,
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int,
    val current: WeatherData
)

data class Forecast(
    val lat: Float,
    val lon: Float,
    val timezone: String,
    @SerializedName("timezone_offset")
    val timezoneOffset: Int,
    val daily: List<WeatherForecastData>
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
    val weather: List<Weather>
)

data class WeatherForecastData(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: TempForecast,
    @SerializedName("feels_like")
    val feelsLike: FeelsLike,
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
    val weather: List<Weather>
)

data class TempForecast(
    val day: Float,
    val min: Float,
    val max: Float,
    val night: Float,
    val eve: Float,
    val morn: Float
)

data class FeelsLike(
    val day: Float,
    val night: Float,
    val eve: Float,
    val morn: Float
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)