package com.example.musicapp.network.musicProfile

data class TimeZone(
    val from: String,
    val playlists: List<Playlists>,
    val to: String
)