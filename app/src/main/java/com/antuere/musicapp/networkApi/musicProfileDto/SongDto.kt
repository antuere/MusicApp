package com.antuere.musicapp.networkApi.musicProfileDto

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




