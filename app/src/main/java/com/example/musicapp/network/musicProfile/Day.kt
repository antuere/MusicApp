package com.example.musicapp.network.musicProfile

import com.example.musicapp.convertDayOfWeekToNumber


data class Day(
    val day: String,
    val timeZones: List<TimeZone>
) : Comparable<Day> {

    override fun compareTo(other: Day): Int {
        val firstDay = convertDayOfWeekToNumber(this.day.uppercase())
        val secondDay = convertDayOfWeekToNumber(other.day.uppercase())

        return firstDay - secondDay
    }

}