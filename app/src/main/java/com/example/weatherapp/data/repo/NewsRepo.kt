package com.example.weatherapp.data.repo

import android.content.Context
import com.example.weatherapp.data.model.NewsResponse
import com.example.weatherapp.data.remote.NewsService
import com.example.weatherapp.util.DateHandle
import org.json.JSONObject
import javax.inject.Inject

class NewsRepo @Inject constructor(private val api: NewsService) {
    val date = DateHandle()

    suspend fun getNews() : NewsResponse {
        val response = api.getNews()
        if(response.isSuccessful){
            return response.body()!!
        }else{
            val errMsg = response.errorBody()?.string()?.let {
                JSONObject(it).getString("error") // or whatever your message is
            } ?: run {
                response.code().toString()
            }

            throw Exception(errMsg)
        }

    }
}