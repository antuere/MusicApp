package com.example.musicapp.screens.titleFragment

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.musicapp.MyPlayer
import com.example.musicapp.network.MusicApi
import com.example.musicapp.network.musicProfile.Day
import com.example.musicapp.network.musicProfile.MusicProfile
import com.example.musicapp.network.musicProfile.Playlist
import com.example.musicapp.network.musicProfile.PlaylistItem
import com.google.android.exoplayer2.ExoPlayer
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.*
import java.lang.Exception
import java.net.URL

private var foldersPathsPrivate = mutableMapOf<String, String>()
val foldersPaths: Map<String, String>
    get() = foldersPathsPrivate

private var playlistItemsPrivate = mutableMapOf<Day, MutableList<PlaylistItem>>()
val playlistItems: Map<Day, MutableList<PlaylistItem>>
    get() = playlistItemsPrivate


class TitleViewModel(applicationMy: Application) : AndroidViewModel(applicationMy) {


    private var _profile = MutableLiveData<MusicProfile>()
    val profile: LiveData<MusicProfile>
        get() = _profile

    private var _player = MutableLiveData<ExoPlayer>()
    val player: LiveData<ExoPlayer>
        get() = _player


    private var _playerExtra = MutableLiveData<ExoPlayer>()
    val playerExtra: LiveData<ExoPlayer>
        get() = _playerExtra

    private var _playlists = MutableLiveData<List<Playlist>>()

    private val _showError = MutableLiveData<String>()
    val showError: LiveData<String>
        get() = _showError

    private val _renderUI = MutableLiveData(false)
    val renderUI: LiveData<Boolean>
        get() = _renderUI


    init {
        firstInitial()
    }


    /*Made its once on start app:
    * 1)get response from server
    * 1.1) if get error from retrofit, then show Toast with error message
    * 2)download songs in the phone
    * 3)after download songs, set schedule for player
    * */
    private fun firstInitial() {
        Timber.i("my log we in the getResponseServer")
        viewModelScope.launch {
            try {
                _profile.value = MusicApi.retrofitService.getJSON()

                _playlists.value = _profile.value!!.schedule.playlists

                downloadMusicFiles()

                _renderUI.value = true
                Timber.i("my log folders : $foldersPaths")

                _player.value =
                    MyPlayer.getInstanceMain(getApplication<Application>().applicationContext)
                _playerExtra.value =
                    MyPlayer.getInstanceExtra(getApplication<Application>().applicationContext)

                _profile.value!!.schedule.days.forEach { day ->
                    val items = mutableListOf<PlaylistItem>()
                    day.timeZones.forEach { timeZone ->
                        timeZone.playlistsOfZone.forEach { playlistsZone ->
                            val item = PlaylistItem(
                                timeZone.from,
                                timeZone.to,
                                playlistsZone.getPlaylist(_playlists.value!!),
                                playlistsZone.proportion
                            )
                            items.add(item)
                            playlistItemsPrivate[day] = items
                        }
                    }
                }

                MyPlayer.setScheduleForPlayer(_profile.value!!)

            } catch (e: Exception) {
                _showError.value = e.message
            }
        }
    }

    //Download ALL songs from profile
    private suspend fun downloadMusicFiles() {
        _playlists.value!!.forEach { playlist ->
            playlist.songs.forEach { song ->
                downloadMusicFileFromUrl(
                    song.url,
                    song.name,
                    getApplication<Application>().applicationContext,
                    playlist.name
                ).join()

            }
        }

    }

    // Download ONE song from URL and write folderPath
    private fun downloadMusicFileFromUrl(
        urlString: String, fileName: String, context: Context, playlist: String
    ): Job {
        Timber.i("my log in download $fileName")
        return viewModelScope.launch(Dispatchers.IO) {
            Timber.i("my log in coroutine")
            val url = URL(urlString)
            url.openConnection().connect()

            val inputStream = BufferedInputStream(url.openStream())

            val directoryString = context.filesDir.absolutePath + "/$playlist/"
            val directory = File(directoryString)

            if (!foldersPaths.containsKey(playlist)) {
                foldersPathsPrivate[playlist] = directoryString
            }

            if (!directory.exists()) {
                Timber.i("my log : make dir $directory")
                directory.mkdir()
            }

            val file = File(directory, fileName)
            val outputStream = FileOutputStream(file)

            val data = ByteArray(1024)
            var count = inputStream.read(data)

            try {
                while (count != -1) {
                    outputStream.write(data, 0, count)
                    count = inputStream.read(data)
                }
            } catch (e: Exception) {
                Timber.i("my log again error, lovely emulator >")
            }

            Timber.i("my log save complete $fileName")

            outputStream.flush()
            outputStream.close()
            inputStream.close()
        }
    }

}