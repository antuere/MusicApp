package com.example.musicapp.network.musicProfileDto

import com.example.musicapp.database.musicProfileEntity.SongEntity
import com.example.musicapp.domain.Song
import com.squareup.moshi.Json


data class SongDto(

    val duration: Int,

    @Json(name = "file_name")
    val url: String,

    @Json(name = "id")
    val fileId: Int,

    @Json(name = "md5_file")
    val md5File: String,

    val name: String,
    val order: Int,
    val size: Int
)




