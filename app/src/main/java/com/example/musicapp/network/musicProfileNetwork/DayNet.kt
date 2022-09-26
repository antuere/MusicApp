package com.example.musicapp.network.musicProfileNetwork

import com.example.musicapp.convertDayOfWeekToNumber
import com.example.musicapp.database.musicProfileDB.DayDB
import com.example.musicapp.domain.Day


data class DayNet(
    val day: String,
    val timeZones: List<TimeZoneNet>
) : Comparable<DayNet> {

    override fun compareTo(other: DayNet): Int {
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

    fun asDayDB() : DayDB {
        return DayDB(
            day = day,
            timeZones = timeZones.map { it.asTimeZoneDB() }
        )
    }
}