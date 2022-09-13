package com.example.musicapp.network.musicProfile

import com.squareup.moshi.Json

data class Schedule(
    val days: List<Day>,

    @Json(name = "playlists")
    val playlistsOfZone: List<PlaylistsZone>
)