package com.example.weatherapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.model.AddressResponse
import com.example.weatherapp.data.repo.OpenMapRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class OpenMapViewModel @Inject constructor(private val repo: OpenMapRepo) : ViewModel() {
    suspend fun getJSON(lat : Float, lon : Float) : AddressResponse {
        return repo.getJSON(lat,lon)
    }

    suspend fun getJSONResponse(lat : Float, lon : Float) : Response<AddressResponse> {
        return repo.getJSONResponse(lat,lon)
    }

    suspend fun getCountry(lat : Float, lon : Float) : String{
        val response = repo.getJSON(lat,lon)
        return response.address.country
    }

    suspend fun getCity(lat : Float, lon : Float) : String{
        val response = repo.getJSON(lat,lon)
        return response.address.city
    }
}