package com.example.weatherapp.util

import androidx.room.paging.util.getOffset
import java.time.LocalDateTime
import java.time.ZoneId

class DateHandle(){
    private val date = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)

    fun getDatePlusDay(days : Long) : Long{
        val response = date.plusDays(days).atZone(ZoneId.systemDefault()).toEpochSecond()
        return response
    }

    fun getDate() : LocalDateTime {
        return date
    }

    fun toUnixTimestamp() : Long{
        return date.atZone(ZoneId.systemDefault()).toEpochSecond()
    }

    fun getUnixTimestampString() : String{
        return date.atZone(ZoneId.systemDefault()).toEpochSecond().toString()
    }
}