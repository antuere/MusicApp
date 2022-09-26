package com.example.musicapp.network.musicProfileNetwork

import com.example.musicapp.database.musicProfileDB.PlaylistDB
import com.example.musicapp.domain.Playlist
import com.squareup.moshi.Json


data class PlaylistNet(
    val duration: Int,

    @Json(name = "files")
    val songs: List<SongNet>,

    val id: Int,
    val name: String,
    val random: Boolean
) {
    fun asPlaylist(): Playlist {
        return Playlist(
            duration = duration,
            songs = songs.map { it.asSong() },
            id = id,
            name = name,
            random = random
        )
    }

    fun asPlaylistDB(): PlaylistDB {
        return PlaylistDB(
            duration = duration,
            songs = songs.map { it.asSongDB() },
            id = id,
            name = name,
            random = random
        )
    }
}