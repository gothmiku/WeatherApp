package com.example.weatherapp.dependencyinjection

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OpenMapRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherOkHttp

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OpenMapOkHttp

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OpenMapGSON

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherGSON