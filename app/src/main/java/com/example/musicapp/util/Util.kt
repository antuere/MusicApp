package com.example.musicapp.util



fun convertDayOfWeekToNumber(day: String, startWeekFromMonday: Boolean = true): Int {

    if (startWeekFromMonday) {
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
    } else {
        return when (day) {
            "MONDAY" -> 2
            "TUESDAY" -> 3
            "WEDNESDAY" -> 4
            "THURSDAY" -> 5
            "FRIDAY" -> 6
            "SATURDAY" -> 7
            "SUNDAY" -> 1
            else -> throw IllegalArgumentException("Invalid day of week")
        }
    }
}













