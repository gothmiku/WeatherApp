package com.example.weatherapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.repo.NewsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repo: NewsRepo) : ViewModel() {
    suspend fun getNews() = repo.getNews()

}