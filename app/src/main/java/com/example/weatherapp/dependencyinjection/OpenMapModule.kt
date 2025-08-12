package com.example.weatherapp.dependencyinjection

import com.example.weatherapp.data.remote.OpenMapAPI
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OpenMapModule {
    @Singleton
    @Provides
    fun provideRetrofit(okhttpClient: OkHttpClient, converterFactory: GsonConverterFactory) : Retrofit {
        return Retrofit.Builder().baseUrl("https://nominatim.openstreetmap.org/").client(okhttpClient).addConverterFactory(converterFactory).build()
    }

    @Singleton
    @Provides
    fun provideOKHTTP() : OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(interceptor).connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Singleton
    @Provides
    fun provideGSON() : GsonConverterFactory {
        return GsonConverterFactory.create(GsonBuilder().setLenient().create())
    }

    @Singleton
    @Provides
    fun provideOpenMapAPI(retrofit: Retrofit) : OpenMapAPI {
        return retrofit.create(OpenMapAPI::class.java)
    }
}