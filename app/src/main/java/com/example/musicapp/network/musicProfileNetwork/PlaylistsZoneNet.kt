package com.example.musicapp.network.musicProfileNetwork

import com.example.musicapp.database.musicProfileDB.PlaylistsZoneDB
import com.example.musicapp.domain.PlaylistsZone
import com.squareup.moshi.Json


data class PlaylistsZoneNet(

    @Json(name = "playlist_id")
    val playlistId: Int,
    var proportion: Int
) {

    fun getPlaylist(list: List<PlaylistNet>): PlaylistNet {

        list.forEach { playlist ->
            if (playlist.id == this.playlistId) {
                return playlist
            }
        }
        throw IllegalArgumentException("Playlist not found")
    }

    fun asPlaylistZone(): PlaylistsZone {
        return PlaylistsZone(playlistId, proportion)
    }

    fun asPlaylistZoneDB(): PlaylistsZoneDB {
        return PlaylistsZoneDB(playlistId, proportion)
    }

}

