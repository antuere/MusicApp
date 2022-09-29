package com.antuere.musicapp.domain.musicProfile

data class Playlist(
    val duration: Int,

    val songs: List<Song>,

    val id: Int,
    val name: String,
    val random: Boolean
)