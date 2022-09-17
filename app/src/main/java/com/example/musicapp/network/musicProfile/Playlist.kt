package com.example.musicapp.network.musicProfile

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Playlist(
    val duration: Int,

    @Json(name = "files")
    val songs: List<Song>,

    val id: Int,
    val name: String,
    val random: Boolean
) : Parcelable