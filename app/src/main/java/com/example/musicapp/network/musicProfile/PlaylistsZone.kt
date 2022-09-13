package com.example.musicapp.network.musicProfile

data class PlaylistsZone(
    val duration: Int,
    val files: List<File>,
    val id: Int,
    val name: String,
    val random: Boolean
)