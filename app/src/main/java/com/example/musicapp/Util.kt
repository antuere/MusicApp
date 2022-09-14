package com.example.musicapp

import android.content.Context
import timber.log.Timber
import java.io.BufferedInputStream
import java.net.URL
import java.nio.Buffer
import java.nio.channels.Channels

fun convertDayOfWeekToNumber(day: String): Int {
    return when (day) {
        "MONDAY" -> 1
        "TUESDAY" -> 2
        "WEDNESDAY" -> 3
        "THURSDAY" -> 4
        "FRIDAY" -> 5
        "SATURDAY" -> 6
        "SUNDAY" -> 7
        else -> throw IllegalArgumentException("Invalid day of week")
    }
}


