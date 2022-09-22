package com.example.musicapp.network.musicProfile



data class Schedule(
    val days: List<Day>,

    val playlists: List<Playlist>
)