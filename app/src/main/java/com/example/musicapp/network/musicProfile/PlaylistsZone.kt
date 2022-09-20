package com.example.musicapp.network.musicProfile

import android.os.Parcelable
import com.squareup.moshi.Json


data class PlaylistsZone(

    @Json(name = "playlist_id")
    val playlistId: Int,
    val proportion: Int
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

