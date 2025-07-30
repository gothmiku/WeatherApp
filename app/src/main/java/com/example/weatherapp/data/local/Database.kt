package com.example.weatherapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.weatherapp.data.model.WeatherInfo
import java.time.LocalDateTime


//@Database
//    (entities = [WeatherInfo::class], version = 1, exportSchema = false)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun weatherInfoDAO() : WeatherInfoDAO
//
//    private class WeatherDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback(){
//        override fun onCreate(db: SupportSQLiteDatabase) {
//            super.onCreate(db)
//            INSTANCE?.let{
//                    database ->
//                scope.launch {
//                    val weatherInfoo = database.weatherInfoDAO()
//                    weatherInfoo.deleteAll()
//                    val weather = WeatherInfo(date=LocalDateTime.now().toString(),temperature="10",humidity="10",windSpeed="10",pressure="10")
//                    weatherInfoo.insertWeatherInfo(weather)
//                }
//            }
//        }
//    }
//
//    companion object {
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//        fun getDatabase(context: Context): AppDatabase {
//            val tempInstance = INSTANCE
//            if (tempInstance != null) {
//                return tempInstance
//            }
//            synchronized(this) {
//                val scope = CoroutineScope(Dispatchers.IO)
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "weather_dbcache"
//                )
//                    .addCallback(WeatherDatabaseCallback(scope))
//                    .build()
//                INSTANCE = instance
//                return instance
//            }
//        }
//    }
//}

@Database(entities = [WeatherInfo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherInfoDAO(): WeatherInfoDAO
}