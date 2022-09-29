package com.antuere.musicapp.domain.usecase

import com.antuere.musicapp.MyPlayer
import com.antuere.musicapp.domain.musicProfile.PlaylistsZone
import com.antuere.musicapp.domain.musicProfile.Song

class GetSongsForPlayerUseCase {

    operator fun invoke(): List<Song> {

        val playlistsZonesSorted = MyPlayer.playlistsRequired.sortedBy { it.proportion }
        val groupedPlaylists: Map<Int, List<PlaylistsZone>> =
            playlistsZonesSorted.groupBy { it.proportion }

        val songs: MutableList<Song> = mutableListOf()
        groupedPlaylists.forEach { (prop, playlists) ->
            playlists.forEach { playlistsZone ->
                val playlist = playlistsZone.getPlaylist(MyPlayer.playlistsDownload)
                for (i in 0 until prop) {
                    val song = playlist.songs.random()
                    songs.add(song)
                }
            }
        }

        return songs
    }
}