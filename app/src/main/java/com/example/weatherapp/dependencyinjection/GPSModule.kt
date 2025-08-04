package com.example.weatherapp.dependencyinjection

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.weatherapp.data.local.AppDatabase
import com.example.weatherapp.data.local.GPSDAO
import com.example.weatherapp.data.local.WeatherInfoDAO
import com.example.weatherapp.data.remote.GPSController
import com.example.weatherapp.data.remote.WeatherAPI
import com.example.weatherapp.data.repo.GPSRepo
import com.example.weatherapp.data.repo.WeatherRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GPSModule {
    @Singleton
    @Provides
    fun provideGPSRepo(gpsDAO: GPSDAO,gpsController: GPSController): GPSRepo {
        return GPSRepo(gpsDAO,gpsController)
    }

    @Singleton
    @Provides
    fun provideGPSController(@ApplicationContext context: Context, gpsDAO: GPSDAO): GPSController {
        return GPSController(context, gpsDAO)
    }

    @Singleton
    @Provides
    fun provideGPSDAO(appDatabase: AppDatabase): GPSDAO {
        return appDatabase.gpsDAO()
    }

}