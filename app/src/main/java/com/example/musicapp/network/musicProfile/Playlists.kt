package com.example.musicapp.network.musicProfile

data class Playlists(
    val duration: Int,
    val files: List<File>,
    val id: Int,
    val name: String,
    val random: Boolean
)