package com.example.musicapp.database.musicProfileDB

import com.example.musicapp.domain.TimeZone
import com.squareup.moshi.Json
import java.text.SimpleDateFormat
import java.util.Locale


data class TimeZoneDB(
    val from: String,

    @Json(name = "playlists")
    val playlistsOfZone: List<PlaylistsZoneDB>,
    val to: String
) : Comparable<TimeZoneDB> {

    override fun compareTo(other: TimeZoneDB): Int {
        val format = SimpleDateFormat("H:mm", Locale.ENGLISH)

        val firstDate = format.parse(this.from)
        val secondDate = format.parse(other.from)

        return firstDate?.compareTo(secondDate)
            ?: throw NullPointerException("First date is null in TimeZone")
    }

    fun asTimeZone(): TimeZone {
        return TimeZone(
            from = from,
            playlistsOfZone = playlistsOfZone.map { it.asPlaylistZone() },
            to = to
        )

    }

}