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

@Database(entities = [WeatherInfo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDAO(): WeatherInfoDAO

    private class WeatherDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // This runs when database is created for the first time
            android.util.Log.d("Database", "Database created for the first time")
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            // This runs every time database is opened
            android.util.Log.d("Database", "Database opened")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "weather_dbcache"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}