package com.example.musicapp.network.musicProfileDto

import com.example.musicapp.database.musicProfileEntity.PlaylistsZoneEntity
import com.example.musicapp.domain.PlaylistsZone
import com.squareup.moshi.Json


data class PlaylistsZoneDto(

    @Json(name = "playlist_id")
    val playlistId: Int,
    var proportion: Int
)




