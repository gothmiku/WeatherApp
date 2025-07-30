package com.example.weatherapp

import android.app.Application

import com.example.weatherapp.data.repo.WeatherRepo
import com.example.weatherapp.data.local.AppDatabase

class WeatherApp : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { WeatherRepo(database.weatherInfoDAO()) }
}