package com.example.musicapp.network.musicProfile

import android.os.Parcelable


data class Schedule(
    val days: List<Day>,

    val playlists: List<Playlist>
)