package com.example.weatherapp.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/*
I am considering to put date as the primary key and reset the database once in a while.
This will be mostly about caching
 */

@Entity(tableName = "WeatherInfo")
data class WeatherInfo(
    @PrimaryKey val date: String,
    val temp: Float,
    val feels_like: Float,
    val humidity: Int,
    val wind_speed: Float,
    val pressure: Int,
    val clouds : Int,
    val uvi : Float,
    val visibility : Int
)

@Entity(tableName = "Coordinates")
data class Coordinates(
    @PrimaryKey val date: String,
    val lat: Float,
    val lon: Float,
)
