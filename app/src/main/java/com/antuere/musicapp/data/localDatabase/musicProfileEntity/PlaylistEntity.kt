package com.antuere.musicapp.data.localDatabase.musicProfileEntity

import com.squareup.moshi.Json


data class PlaylistEntity(
    val duration: Int,

    @Json(name = "files")
    val songs: List<SongEntity>,

    val id: Int,
    val name: String,
    val random: Boolean
)
