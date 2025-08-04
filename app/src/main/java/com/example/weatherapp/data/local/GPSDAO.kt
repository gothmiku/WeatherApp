package com.example.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.weatherapp.data.model.Coordinates
import com.example.weatherapp.data.model.WeatherInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface GPSDAO{
    @Insert
    suspend fun insertGPSInfo(coordinates: Coordinates)

    @Query("SELECT * FROM Coordinates")
    fun getAllCoordinatesFlow(): Flow<List<Coordinates>>

    @Query("SELECT * FROM Coordinates ORDER BY date DESC LIMIT 1")
    suspend fun latestsGPSInfo() : Coordinates?

    @Query("DELETE FROM Coordinates")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM Coordinates")
    suspend fun gpsInfoCount() : Int

    @Query("SELECT * FROM Coordinates")
    suspend fun getAllGPSInfo() : List<Coordinates>

    @Query("SELECT * FROM Coordinates WHERE date = :date")
    suspend fun getGPSInfoByDate(date: String): Coordinates?
}