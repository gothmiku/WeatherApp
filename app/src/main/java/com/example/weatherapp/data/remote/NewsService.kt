package com.example.weatherapp.data.remote

import com.example.weatherapp.BuildConfig.NEWS_KEY
import com.example.weatherapp.data.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService{
    @GET("top-headlines")
    suspend fun getNews(
        @Query("country") country : String = "us",
        @Query("apiKey") key : String = NEWS_KEY
    ) : Response<NewsResponse>
}