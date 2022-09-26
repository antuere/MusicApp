package com.example.musicapp.database.musicProfileDB

import com.example.musicapp.domain.Song
import com.squareup.moshi.Json
import timber.log.Timber
import java.io.File
import java.security.MessageDigest


data class SongDB(

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
) : Comparable<SongDB> {

    lateinit var playlist: String
    lateinit var pathToFile: String


    fun asSong(): Song {
        return Song(
            duration, url, fileId, md5File, name, order, size
        )
    }

    override fun compareTo(other: SongDB): Int {
        return this.order - other.order
    }


    override fun toString(): String {
        return this.name
    }
}
