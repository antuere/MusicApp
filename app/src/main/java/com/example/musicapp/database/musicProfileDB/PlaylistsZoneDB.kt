package com.example.musicapp.database.musicProfileDB

import com.example.musicapp.domain.PlaylistsZone
import com.squareup.moshi.Json


data class PlaylistsZoneDB(

    @Json(name = "playlist_id")
    val playlistId: Int,
    var proportion: Int
) {

    fun asPlaylistZone(): PlaylistsZone {
        return PlaylistsZone(playlistId, proportion)
    }
}

