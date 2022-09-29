package com.antuere.domain.musicProfile


data class PlaylistsZone(

    val playlistId: Int,
    var proportion: Int
) {

    fun getPlaylist(list: List<Playlist>): Playlist {

        list.forEach { playlist ->
            if (playlist.id == this.playlistId) {
                return playlist
            }
        }
        throw IllegalArgumentException("Playlist not found")
    }

}

