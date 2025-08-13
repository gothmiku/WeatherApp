package com.example.weatherapp.data.repo

import com.example.weatherapp.data.local.AddressCacheDAO
import com.example.weatherapp.data.model.AddressResponseCache
import com.example.weatherapp.data.remote.GPSController
import javax.inject.Inject

class CacheRepo @Inject constructor(private val dao: AddressCacheDAO, private val GPSController : GPSController){
    suspend fun insertAddressCache(addressCache: AddressResponseCache){
        dao.insertAddressCache(addressCache)
    }
    suspend fun getAddressById(id: Int): AddressResponseCache?{
        return dao.getAddressCacheById(id)
    }
    suspend fun deleteAddress(addressCache: AddressResponseCache){
        dao.deleteAddressCache(addressCache)
    }
    suspend fun deleteAllAddressCaches(){
        dao.deleteAllAddressCaches()
    }
    suspend fun getLatestAddressCache(): AddressResponseCache?{
        return dao.getLatestAddressCache()
    }
    suspend fun getAddressCacheCount(): Int{
        return dao.getAddressCacheCount()
    }
    fun haversine(lat: Float, lon: Float, secondLat: Float, secondLon: Float): Double {
        return GPSController.haversine(lat, lon, secondLat, secondLon)
    }


}