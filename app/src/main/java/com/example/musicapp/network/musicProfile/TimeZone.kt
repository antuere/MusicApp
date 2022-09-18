package com.example.musicapp.network.musicProfile

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*


@Parcelize
data class TimeZone(
    val from: String,

    @Json(name = "playlists")
    val playlistsOfZone: List<PlaylistsZone>,
    val to: String
) : Comparable<TimeZone>, Parcelable {

    override fun compareTo(other: TimeZone): Int {
        val format = SimpleDateFormat("H:mm", Locale.ENGLISH)

        val firstDate = format.parse(this.from)
        val secondDate = format.parse(other.from)

        return firstDate?.compareTo(secondDate) ?: throw NullPointerException("First date is null in TimeZone")
    }

}