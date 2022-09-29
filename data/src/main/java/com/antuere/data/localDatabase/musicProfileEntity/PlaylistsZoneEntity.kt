package com.antuere.data.localDatabase.musicProfileEntity

import com.squareup.moshi.Json


data class PlaylistsZoneEntity(

    @Json(name = "playlist_id")
    val playlistId: Int,
    var proportion: Int
)



