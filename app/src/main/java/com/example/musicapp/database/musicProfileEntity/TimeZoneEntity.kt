package com.example.musicapp.database.musicProfileEntity

import com.squareup.moshi.Json


data class TimeZoneEntity(
    val from: String,

    @Json(name = "playlists")
    val playlistsOfZone: List<PlaylistsZoneEntity>,
    val to: String
)


