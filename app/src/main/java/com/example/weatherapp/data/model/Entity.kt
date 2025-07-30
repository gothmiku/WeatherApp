package com.example.weatherapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
I am considering to put date as the primary key and reset the database once in a while.
This will be mostly about caching
 */

@Entity
data class WeatherInfo(
    @PrimaryKey val date: String,
    val temperature: String,
    val humidity: String,
    val windSpeed: String,
    val pressure: String
)
