package com.example.musicapp.database.musicProfileEntity

import com.example.musicapp.domain.PlaylistsZone
import com.squareup.moshi.Json


data class PlaylistsZoneEntity(

    @Json(name = "playlist_id")
    val playlistId: Int,
    var proportion: Int
)



