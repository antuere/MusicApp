package com.example.musicapp.network.musicProfile

import android.os.Parcelable
import com.example.musicapp.convertDayOfWeekToNumber
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


@Parcelize
data class Day(
    val day: String,
    val timeZones: List<TimeZone>
) : Comparable<Day>, Parcelable {

    override fun compareTo(other: Day): Int {
        val firstDay = convertDayOfWeekToNumber(this.day.uppercase())
        val secondDay = convertDayOfWeekToNumber(other.day.uppercase())

        return firstDay - secondDay
    }

}