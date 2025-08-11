package com.example.weatherapp.data.model

import com.example.weatherapp.R


enum class WeatherIcons (val icon: Int, val weatherDescription: List<String>) {
    SUNNY(R.drawable.sunny, listOf("clear sky","few clouds")),
    CLOUDY(R.drawable.cloud, listOf("scattered clouds","broken clouds")),
    RAINY(R.drawable.rainy,listOf("light rain","shower rain","rain")),
    SNOWY(R.drawable.snowy2,listOf("snow")),
    STORMY(R.drawable.stormy,listOf("thunderstorm")),
    FOGGY(R.drawable.misty,listOf("mist"));


    companion object {
        fun fromApiDesc(name: String): WeatherIcons {
            return entries.firstOrNull { type ->
                type.weatherDescription.any { it.equals(name, ignoreCase = true) }
            } ?: SUNNY // fallback
        }
    }
}

enum class WeatherIconsSkewed (val icon: Int, val weatherDescription: List<String>) {
    SUNNY(R.drawable.sunny_skew, listOf("clear sky","few clouds")),
    CLOUDY(R.drawable.cloud_skew, listOf("scattered clouds","broken clouds")),
    RAINY(R.drawable.rainy_skew,listOf("light rain","shower rain","rain")),
    SNOWY(R.drawable.snowy_skew,listOf("snow")),
    STORMY(R.drawable.thunderstorm_skew,listOf("thunderstorm")),
    FOGGY(R.drawable.misty_skew,listOf("mist"));


    companion object {
        fun fromApiDesc(name: String): WeatherIconsSkewed {
            return entries.firstOrNull { type ->
                type.weatherDescription.any { it.equals(name, ignoreCase = true) }
            } ?: SUNNY // fallback
        }
    }
}