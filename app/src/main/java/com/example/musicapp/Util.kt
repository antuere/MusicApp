package com.example.musicapp

import com.example.musicapp.network.musicProfile.Playlist
import com.example.musicapp.network.musicProfile.PlaylistsZone
import com.example.musicapp.network.musicProfile.Song
import com.google.android.exoplayer2.ExoPlayer
import timber.log.Timber
import java.io.File
import java.security.MessageDigest

var foldersPaths = mutableMapOf<String, String>()

fun convertDayOfWeekToNumber(day: String): Int {
    return when (day) {
        "MONDAY" -> 1
        "TUESDAY" -> 2
        "WEDNESDAY" -> 3
        "THURSDAY" -> 4
        "FRIDAY" -> 5
        "SATURDAY" -> 6
        "SUNDAY" -> 7
        else -> throw IllegalArgumentException("Invalid day of week")
    }
}

fun Song.checkMD5(pathToFile: String?): Boolean {

    val digest = MessageDigest.getInstance("MD5")
    val requestMD5 = this.md5File
    var result: String

    val file = File(pathToFile!!)

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

fun PlaylistsZone.getPlaylist(list: List<Playlist>): Playlist {

    list.forEach { playlist ->
        if (playlist.id == this.playlistId) {
            return playlist
        }
    }
    throw IllegalArgumentException("Playlist not found")
}

object MyPlayer {
    lateinit var player: ExoPlayer
}







