package com.example.musicapp.domain

import com.squareup.moshi.Json


data class PlaylistsZone(

    @Json(name = "playlist_id")
    val playlistId: Int,
    var proportion: Int
)  {

    fun getPlaylist(list: List<Playlist>): Playlist {

        list.forEach { playlist ->
            if (playlist.id == this.playlistId) {
                return playlist
            }
        }
        throw IllegalArgumentException("Playlist not found")
    }
}

