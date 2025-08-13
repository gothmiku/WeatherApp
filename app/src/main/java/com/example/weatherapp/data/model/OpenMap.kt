package com.example.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class AddressResponse(
    @SerializedName("address")
    val address: Address
)

data class Address(
    @SerializedName("town")
    val city: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("country_code")
    val countryCode: String
)