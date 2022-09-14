package com.example.musicapp.network.musicProfile

import com.squareup.moshi.Json

data class File(
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
) : Comparable<File> {


    override fun compareTo(other: File): Int {
        return this.order - other.order
    }

}
