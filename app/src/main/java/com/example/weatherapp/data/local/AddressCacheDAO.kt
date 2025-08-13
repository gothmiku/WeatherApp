package com.example.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.weatherapp.data.model.AddressResponseCache
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressCacheDAO {
    @Insert
    suspend fun insertAddressCache(addressCache: AddressResponseCache)

    @Query("SELECT * FROM AddressResponseCache WHERE id = :id")
    suspend fun getAddressCacheById(id: Int): AddressResponseCache?

    @Delete
    suspend fun deleteAddressCache(addressCache: AddressResponseCache)

    @Query("DELETE FROM AddressResponseCache")
    suspend fun deleteAllAddressCaches()

    @Query("SELECT * FROM AddressResponseCache")
    suspend fun getAllAddressCaches(): List<AddressResponseCache>

    @Query("SELECT * FROM AddressResponseCache")
    fun getAllAddressCachesFlow(): Flow<List<AddressResponseCache>>

    @Query("SELECT * FROM AddressResponseCache ORDER BY id DESC LIMIT 1")
    suspend fun getLatestAddressCache(): AddressResponseCache?

    @Query("SELECT COUNT(*) FROM AddressResponseCache")
    suspend fun getAddressCacheCount(): Int

    @Query("SELECT * FROM AddressResponseCache WHERE lat = :lat AND lon = :lon")
    suspend fun getAddressCacheByLatLon(lat: Float, lon: Float): AddressResponseCache?


}