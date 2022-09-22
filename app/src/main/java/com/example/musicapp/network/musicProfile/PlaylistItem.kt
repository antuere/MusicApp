package com.example.musicapp.network.musicProfile

import java.text.SimpleDateFormat
import java.util.*

data class PlaylistItem(

    val from: String,
    val to: String,

    val playlist: Playlist,
    val proportion: Int
) : Comparable<TimeZone> {

    override fun compareTo(other: TimeZone): Int {
        val format = SimpleDateFormat("H:mm", Locale.ENGLISH)

        val firstDate = format.parse(this.from)
        val secondDate = format.parse(other.from)

        return firstDate?.compareTo(secondDate)
            ?: throw NullPointerException("First date is null in TimeZone")
    }

}