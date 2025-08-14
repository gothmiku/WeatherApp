package com.example.weatherapp.dependencyinjection

import com.example.weatherapp.data.remote.NewsService
import com.example.weatherapp.data.remote.WeatherAPI
import com.example.weatherapp.data.repo.NewsRepo
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
object NewsModule{
    @NewsRetrofit
    @Singleton
    @Provides
    fun provideNewsRetrofit(@NewsOkHttp okhttpClient: OkHttpClient, @NewsGSON converterFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .client(okhttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }
    @NewsGSON
    @Singleton
    @Provides
    fun provideGSONConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create(GsonBuilder().setLenient().create())
    }

    @NewsOkHttp
    @Singleton
    @Provides
    fun provideNewsOKHTTP(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsService(@NewsRetrofit retrofit: Retrofit): NewsService {
        return retrofit.create(NewsService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewsRepo(newsService: NewsService) : NewsRepo {
        return NewsRepo(newsService)
    }
}