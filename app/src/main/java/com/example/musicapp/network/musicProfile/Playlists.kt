package com.example.musicapp.network.musicProfile

import com.squareup.moshi.Json

data class Playlists(

    @Json(name = "playlist_id")
    val playlistId: Int,
    val proportion: Int
)