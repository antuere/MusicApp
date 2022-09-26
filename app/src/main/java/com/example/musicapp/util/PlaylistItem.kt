package com.example.musicapp.util

import com.example.musicapp.domain.Playlist
import com.example.musicapp.network.musicProfileNetwork.PlaylistNet
import com.example.musicapp.network.musicProfileNetwork.TimeZoneNet
import java.text.SimpleDateFormat
import java.util.*


// Class for convenient interaction and display of information in nested RecycleView
data class PlaylistItem(

    val from: String,
    val to: String,

    val playlist: Playlist,
) : Comparable<TimeZoneNet> {

    override fun compareTo(other: TimeZoneNet): Int {
        val format = SimpleDateFormat("H:mm", Locale.ENGLISH)

        val firstDate = format.parse(this.from)
        val secondDate = format.parse(other.from)

        return firstDate?.compareTo(secondDate)
            ?: throw NullPointerException("First date is null in TimeZone")
    }

}