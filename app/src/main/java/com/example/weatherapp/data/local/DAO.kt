package com.example.weatherapp.data.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Dao
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import com.example.weatherapp.data.model.WeatherInfo

@Dao
interface WeatherInfoDAO{
    @Insert
    suspend fun insertWeatherInfo(weatherInfo: WeatherInfo)

    @Query("SELECT * FROM WeatherInfo WHERE date = :date")
    fun getWeatherInfoByDate(date: String): WeatherInfo?

    @Query("DELETE FROM WeatherInfo")
    fun deleteAllWeatherInfo()

    @Query("SELECT * FROM WeatherInfo")
    fun getAllWeatherInfo(): List<WeatherInfo>

    @Query("SELECT * FROM WeatherInfo")
    fun getAllWeatherInfoFlow(): Flow<List<WeatherInfo>>

    @Query("SELECT COUNT(*) FROM WeatherInfo")
    fun getWeatherInfoCount(): Int

    @Query("SELECT * FROM WeatherInfo ORDER BY date DESC LIMIT 1")
    fun getLatestWeatherInfo(): WeatherInfo?

    @Query("SELECT * FROM WeatherInfo ORDER BY date ASC LIMIT 1")
    fun getOldestWeatherInfo(): WeatherInfo?

    @Query("DELETE FROM WeatherInfo WHERE date = (SELECT date FROM WeatherInfo ORDER BY date ASC LIMIT 1)")
    fun deleteOldestWeatherInfo()

    @Query("DELETE FROM WeatherInfo")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM WeatherInfo")
    fun countRows(): Int

    @Delete(entity = WeatherInfo::class)
    fun delete(weatherInfo: WeatherInfo)

    @Update(entity = WeatherInfo::class)
    fun update(weatherInfo: WeatherInfo)


}