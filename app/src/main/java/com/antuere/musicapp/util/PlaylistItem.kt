package com.antuere.musicapp.util

import com.antuere.domain.musicProfile.Playlist
import com.antuere.domain.musicProfile.PlaylistsZone
import java.text.SimpleDateFormat
import java.util.*


// Class for convenient interaction and display of information in nested RecycleView
data class PlaylistItem(

    val from: String,
    val to: String,
    val playlist: Playlist,
    val showError: Boolean,
    val playlistsZone: PlaylistsZone
) : Comparable<PlaylistItem> {

    override fun compareTo(other: PlaylistItem): Int {
        val format = SimpleDateFormat("H:mm", Locale.ENGLISH)

        val firstDate = format.parse(this.from)
        val secondDate = format.parse(other.from)

        return firstDate?.compareTo(secondDate)
            ?: throw NullPointerException("First date is null in TimeZone")
    }

}