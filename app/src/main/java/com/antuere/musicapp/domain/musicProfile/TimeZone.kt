package com.antuere.musicapp.domain.musicProfile



data class TimeZone(
    val from: String,
    val playlistsOfZone: List<PlaylistsZone>,
    val to: String
)