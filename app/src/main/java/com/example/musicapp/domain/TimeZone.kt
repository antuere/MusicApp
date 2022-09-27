package com.example.musicapp.domain



data class TimeZone(
    val from: String,
    val playlistsOfZone: List<PlaylistsZone>,
    val to: String
)