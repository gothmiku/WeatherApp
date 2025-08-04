package com.example.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.weatherapp.data.model.Coordinates

@Dao
interface GPSDAO{
    @Insert
    suspend fun insertGPSInfo(coordinates: Coordinates)

    @Query("SELECT * FROM Cordinates ORDER BY date DESC LIMIT 1")
    suspend fun latestsGPSInfo() : Coordinates?

    @Query("DELETE FROM Cordinates")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM Cordinates")
    suspend fun gpsInfoCount() : Int

    @Query("SELECT * FROM Cordinates")
    suspend fun getAllGPSInfo() : List<Coordinates>

    @Query("SELECT * FROM Cordinates WHERE date = :date")
    suspend fun getGPSInfoByDate(date: String): Coordinates?
}