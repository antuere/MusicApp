package com.antuere.musicapp.data.localDatabase.musicProfileEntity

import com.squareup.moshi.Json


data class SongEntity(

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
) {

    lateinit var playlist: String
    lateinit var pathToFile: String

}
