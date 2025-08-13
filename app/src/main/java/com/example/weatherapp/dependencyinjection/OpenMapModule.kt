package com.example.weatherapp.dependencyinjection

import com.example.weatherapp.data.local.AddressCacheDAO
import com.example.weatherapp.data.local.AppDatabase
import com.example.weatherapp.data.remote.GPSController
import com.example.weatherapp.data.remote.OpenMapAPI
import com.example.weatherapp.data.repo.CacheRepo
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
    @OpenMapRetrofit
    @Singleton
    @Provides
    fun provideRetrofit(@OpenMapOkHttp okhttpClient: OkHttpClient,@OpenMapGSON converterFactory: GsonConverterFactory) : Retrofit {
        return Retrofit.Builder().baseUrl("https://nominatim.openstreetmap.org/").client(okhttpClient).addConverterFactory(converterFactory).build()
    }
    @OpenMapOkHttp
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
    @OpenMapGSON
    @Singleton
    @Provides
    fun provideGSON() : GsonConverterFactory {
        return GsonConverterFactory.create(GsonBuilder().setLenient().create())
    }

    @Singleton
    @Provides
    fun provideOpenMapAPI(@OpenMapRetrofit retrofit: Retrofit) : OpenMapAPI {
        return retrofit.create(OpenMapAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideAddressCacheDAO(appDatabase: AppDatabase) : AddressCacheDAO {
        return appDatabase.addressCacheDAO()
    }

    @Singleton
    @Provides
    fun provideCacheRepo(addressCacheDAO: AddressCacheDAO,gps : GPSController) : CacheRepo {
        return CacheRepo(addressCacheDAO,gps)
    }


}