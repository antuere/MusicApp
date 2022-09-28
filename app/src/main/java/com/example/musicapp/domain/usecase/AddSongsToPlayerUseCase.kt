package com.example.musicapp.domain.usecase

import android.net.Uri
import com.example.musicapp.MyPlayer
import com.example.musicapp.domain.PlaylistsZone
import com.example.musicapp.domain.Song
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import java.io.File

class AddSongsToPlayerUseCase(private val player: ExoPlayer, private val playerExtra : ExoPlayer) {

    operator fun invoke(){

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
        if (songs.isNotEmpty()) {
            songs.forEach {
//                Add check MD5 when run on real device
                if (true) {
                    val uri = Uri.fromFile(File(it.pathToFile))
                    val mediaItem = MediaItem.fromUri(uri)

                    val song = mediaItem.mediaMetadata.buildUpon()
                        .setTitle("${it.playlist} - ${it.name}").build()

                    val resultSong = mediaItem.buildUpon().setMediaMetadata(song).build()

                    player.addMediaItem(resultSong)
                    playerExtra.addMediaItem(resultSong)
                }
            }
        }
    }
}