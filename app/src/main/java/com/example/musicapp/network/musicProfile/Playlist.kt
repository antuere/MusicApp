package com.example.musicapp.network.musicProfile

import com.squareup.moshi.Json


data class Playlist(
    val duration: Int,

    @Json(name = "files")
    val songs: List<Song>,

    val id: Int,
    val name: String,
    val random: Boolean
)