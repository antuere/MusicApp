package com.example.musicapp.network.musicProfile

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Schedule(
    val days: List<Day>,


    val playlists: List<Playlist>
) : Parcelable