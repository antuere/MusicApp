package com.example.musicapp.database.musicProfileDB

import com.example.musicapp.domain.Playlist
import com.squareup.moshi.Json


data class PlaylistDB(
    val duration: Int,

    @Json(name = "files")
    val songs: List<SongDB>,

    val id: Int,
    val name: String,
    val random: Boolean
) {
    fun asPlaylist() : Playlist {
        return Playlist(
            duration = duration,
            songs = songs.map { it.asSong() },
            id = id,
            name = name,
            random = random
        )
    }
}