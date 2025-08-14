package com.example.weatherapp.data.model

import com.google.gson.annotations.SerializedName

// AI Saved me so much time

/**
 * Main response data class for the news API
 */
data class NewsResponse(
    @SerializedName("status")
    val status: String,

    @SerializedName("totalResults")
    val totalResults: Int,

    @SerializedName("articles")
    val articles: List<Article>
)

/**
 * Article data class representing individual news articles
 */
data class Article(
    @SerializedName("source")
    val source: Source,

    @SerializedName("author")
    val author: String?,

    @SerializedName("title")
    val title: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("url")
    val url: String?,

    @SerializedName("urlToImage")
    val urlToImage: String?,

    @SerializedName("publishedAt")
    val publishedAt: String?,

    @SerializedName("content")
    val content: String?
)

/**
 * Source data class for news source information
 */
data class Source(
    @SerializedName("id")
    val id: String?,

    @SerializedName("name")
    val name: String
)

/**
 * Example usage with Retrofit client
 */
/*
class NewsRepository {
    private val newsApiService: NewsApiService = RetrofitClient.create()

    suspend fun searchNews(query: String, apiKey: String): Result<NewsResponse> {
        return try {
            val response = newsApiService.getNews(query, apiKey)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTopHeadlines(country: String, apiKey: String): Result<NewsResponse> {
        return try {
            val response = newsApiService.getTopHeadlines(country, apiKey)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
*/