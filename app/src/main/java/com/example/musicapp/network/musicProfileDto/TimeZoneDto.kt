package com.example.musicapp.network.musicProfileDto

import com.squareup.moshi.Json


data class TimeZoneDto(
    val from: String,

    @Json(name = "playlists")
    val playlistsOfZone: List<PlaylistsZoneDto>,
    val to: String
)


