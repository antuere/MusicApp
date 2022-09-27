package com.example.musicapp.network.musicProfileDto

import com.example.musicapp.database.musicProfileEntity.TimeZoneEntity
import com.example.musicapp.domain.TimeZone
import com.squareup.moshi.Json


data class TimeZoneDto(
    val from: String,

    @Json(name = "playlists")
    val playlistsOfZone: List<PlaylistsZoneDto>,
    val to: String
)


