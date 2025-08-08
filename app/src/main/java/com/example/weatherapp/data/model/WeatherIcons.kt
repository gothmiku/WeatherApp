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