package com.example.musicapp.network.musicProfile

import com.squareup.moshi.Json
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

data class TimeZone(
    val from: String,

    @Json(name = "playlists")
    val playlistsOfZone: List<PlaylistsZone>,
    val to: String
) : Comparable<TimeZone> {

    override fun compareTo(other: TimeZone): Int {
        val format = SimpleDateFormat("H:mm", Locale.ENGLISH)

        val firstDate = format.parse(this.from)
        val secondDate = format.parse(other.from)

        return firstDate?.compareTo(secondDate) ?: throw NullPointerException("First date is null in TimeZone")
    }

}