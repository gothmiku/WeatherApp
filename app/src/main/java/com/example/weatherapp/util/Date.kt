package com.example.weatherapp.util

import java.time.LocalDateTime
import java.time.ZoneId

class DateHandle(){
    private val date = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0)

    fun getDatePlusDay(days : Long){
        date.plusDays(days.toLong())
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