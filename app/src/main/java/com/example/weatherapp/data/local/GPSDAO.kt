package com.example.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.weatherapp.data.model.Coordinates
import kotlinx.coroutines.flow.Flow

@Dao
interface GPSDAO {
    @Insert
    suspend fun insertGPSInfo(coordinates: Coordinates)

    @Query("SELECT * FROM Coordinates ORDER BY date ASC")
    fun getAllCoordinatesFlow(): Flow<List<Coordinates>>

    @Query("SELECT * FROM Coordinates ORDER BY date DESC LIMIT 1")
    suspend fun latestGPSInfo(): Coordinates?

    @Query("DELETE FROM Coordinates")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM Coordinates")
    suspend fun gpsInfoCount(): Int

    @Query("SELECT * FROM Coordinates ORDER BY date ASC")
    suspend fun getAllGPSInfo(): List<Coordinates>

    @Query("SELECT * FROM Coordinates WHERE date = :date")
    suspend fun getGPSInfoByDate(date: String): Coordinates?

    @Query("SELECT COUNT(*) FROM Coordinates")
    suspend fun getGPSInfoCount(): Int
}