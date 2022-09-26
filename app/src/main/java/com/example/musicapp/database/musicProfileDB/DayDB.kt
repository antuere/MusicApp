package com.example.musicapp.database.musicProfileDB

import com.example.musicapp.convertDayOfWeekToNumber
import com.example.musicapp.domain.Day


data class DayDB(

    val day: String,

    val timeZones: List<TimeZoneDB>

) : Comparable<DayDB> {

    override fun compareTo(other: DayDB): Int {
        val firstDay = convertDayOfWeekToNumber(this.day.uppercase())
        val secondDay = convertDayOfWeekToNumber(other.day.uppercase())

        return firstDay - secondDay
    }

    fun asDay() : Day {
        return Day(
            day = day,
            timeZones = timeZones.map { it.asTimeZone() }
        )
    }

}