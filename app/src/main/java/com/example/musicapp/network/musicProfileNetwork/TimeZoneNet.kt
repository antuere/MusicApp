package com.example.musicapp.network.musicProfileNetwork

import com.example.musicapp.database.musicProfileDB.TimeZoneDB
import com.example.musicapp.domain.TimeZone
import com.squareup.moshi.Json
import java.text.SimpleDateFormat
import java.util.*


data class TimeZoneNet(
    val from: String,

    @Json(name = "playlists")
    val playlistsOfZone: List<PlaylistsZoneNet>,
    val to: String
) : Comparable<TimeZoneNet> {

    override fun compareTo(other: TimeZoneNet): Int {
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

    fun asTimeZoneDB(): TimeZoneDB {
        return TimeZoneDB(
            from = from,
            playlistsOfZone = playlistsOfZone.map { it.asPlaylistZoneDB() },
            to = to
        )
    }
}