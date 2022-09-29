package com.antuere.domain.usecase

import com.antuere.domain.musicProfile.Playlist
import com.antuere.domain.musicProfile.PlaylistsZone
import com.antuere.domain.musicProfile.Song

class GetSongsForPlayerUseCase(
    private val playlistsRequired: List<PlaylistsZone>,
    private val playlistsDownload: List<Playlist>
) {

    operator fun invoke(): List<Song> {

        val playlistsSorted = playlistsRequired.sortedBy { it.proportion }
        val groupedPlaylists: Map<Int, List<PlaylistsZone>> =
            playlistsSorted.groupBy { it.proportion }

        val songs: MutableList<Song> = mutableListOf()
        groupedPlaylists.forEach { (prop, playlists) ->
            playlists.forEach { playlistsZone ->
                val playlist = playlistsZone.getPlaylist(playlistsDownload)
                for (i in 0 until prop) {
                    val song = playlist.songs.random()
                    songs.add(song)
                }
            }
        }

        return songs
    }
}