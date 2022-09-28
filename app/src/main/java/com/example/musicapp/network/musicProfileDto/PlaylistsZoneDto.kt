package com.example.musicapp.network.musicProfileDto

import com.squareup.moshi.Json


data class PlaylistsZoneDto(

    @Json(name = "playlist_id")
    val playlistId: Int,
    var proportion: Int
)




