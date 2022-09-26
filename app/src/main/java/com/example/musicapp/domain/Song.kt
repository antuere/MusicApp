package com.example.musicapp.domain

import com.squareup.moshi.Json
import timber.log.Timber
import java.io.File
import java.security.MessageDigest


data class Song(

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
) : Comparable<Song> {

    lateinit var playlist: String
    lateinit var pathToFile : String

    override fun compareTo(other: Song): Int {
        return this.order - other.order
    }

    fun checkMD5(): Boolean {

        val digest = MessageDigest.getInstance("MD5")
        val requestMD5 = this.md5File
        var result: String

        val file = File(pathToFile)

        file.inputStream().use { fis ->
            val buffer = ByteArray(1024)
            generateSequence {
                when (val bytesRead = fis.read(buffer)) {
                    -1 -> null
                    else -> bytesRead
                }
            }.forEach { bytesRead ->
                digest.update(buffer, 0, bytesRead)
            }
            result = digest.digest().joinToString("") { "%02x".format(it) }
        }
        Timber.i("md5 for ${file.name} result is: $result")
        Timber.i("md5 for ${file.name} request is: $requestMD5")

        return result == requestMD5
    }

    override fun toString(): String {
        return this.name
    }
}
