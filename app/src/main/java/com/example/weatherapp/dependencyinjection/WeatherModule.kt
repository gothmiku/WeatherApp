package com.example.weatherapp.dependencyinjection

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.weatherapp.data.local.AppDatabase
import com.example.weatherapp.data.local.WeatherInfoDAO
import com.example.weatherapp.data.remote.WeatherAPI
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
object WeatherAppModule {
    @Singleton
    @Provides
    fun provideWeatherRepo(weatherInfoDAO: WeatherInfoDAO) : WeatherRepo {
        return WeatherRepo(weatherInfoDAO)
    }

    @Singleton
    @Provides
    fun provideWeatherInfoDAO(appDatabase: AppDatabase) : WeatherInfoDAO {
        return appDatabase.weatherInfoDAO()
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) : AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "weather_dbcache").build()
    }



}