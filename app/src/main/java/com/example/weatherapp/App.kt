package com.example.weatherapp

import android.app.Application
import com.example.weatherapp.data.repo.WeatherRepo
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application(){
    @Inject
    lateinit var repo : WeatherRepo

    override fun onCreate() {
        super.onCreate()

    }
}