package com.antuere.musicapp.networkApi.musicProfileDto

import com.squareup.moshi.Json


data class PlaylistDto(
    val duration: Int,

    @Json(name = "files")
    val songs: List<SongDto>,

    val id: Int,
    val name: String,
    val random: Boolean
)

