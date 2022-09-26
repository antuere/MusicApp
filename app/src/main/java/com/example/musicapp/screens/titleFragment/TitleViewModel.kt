package com.example.musicapp.screens.titleFragment

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.musicapp.MyPlayer
import com.example.musicapp.database.getDataBase
import com.example.musicapp.domain.Playlist
import com.example.musicapp.repository.MusicProfileRepository
import com.example.musicapp.util.PlaylistItem
import com.google.android.exoplayer2.ExoPlayer
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.*
import java.lang.Exception
import java.net.URL

private var foldersPathsPrivate = mutableMapOf<String, String>()
val foldersPaths: Map<String, String>
    get() = foldersPathsPrivate

private var playlistItemsPrivate = mutableMapOf<String, List<PlaylistItem>>()
val playlistItems: Map<String, List<PlaylistItem>>
    get() = playlistItemsPrivate


class TitleViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDataBase(application)

    private val musicProfileRepository = MusicProfileRepository(database, "Test Profile")

    private var _player = MutableLiveData<ExoPlayer>()
    val player: LiveData<ExoPlayer>
        get() = _player

    private var _playerExtra = MutableLiveData<ExoPlayer>()
    val playerExtra: LiveData<ExoPlayer>
        get() = _playerExtra

    private val _showError = MutableLiveData<String>()
    val showError: LiveData<String>
        get() = _showError

    private val _renderUI = MutableLiveData(false)
    val renderUI: LiveData<Boolean>
        get() = _renderUI


    init {
        refreshProfileFromNetwork()
    }

    var profile = musicProfileRepository.musicProfile


    /*Made its once on start app:
    * 1)get response from server
    * 1.1) if get error from retrofit, then show Toast with error message
    * 2)download songs in the phone
    * 3)after download songs, set schedule for player
    * */
    private fun refreshProfileFromNetwork() {
        viewModelScope.launch {
            try {
                musicProfileRepository.updateProfile()

                delay(400)

                profile = musicProfileRepository.musicProfile

                downloadMusicFiles()

                _player.value =
                    MyPlayer.getInstanceMain(getApplication<Application>().applicationContext)
                _playerExtra.value =
                    MyPlayer.getInstanceExtra(getApplication<Application>().applicationContext)

                profile.value!!.schedule.days.forEach { day ->
                    val items = mutableListOf<PlaylistItem>()
                    day.timeZones.forEach { timeZone ->
                        timeZone.playlistsOfZone.forEach { playlistsZone ->
                            val item = PlaylistItem(
                                timeZone.from,
                                timeZone.to,
                                playlistsZone.getPlaylist(profile.value!!.schedule.playlists),
                            )
                            items.add(item)
                            playlistItemsPrivate[day.day] = items
                        }
                    }
                }

                MyPlayer.setScheduleForPlayer(profile.value!!)

                _renderUI.value = true

            } catch (e: Exception) {
                _showError.value = e.message
            }
        }
    }

    //Download ALL songs from profile
    private suspend fun downloadMusicFiles() {
        Timber.i("my log in downloadAllMusic")
        profile.value!!.schedule.playlists.forEach { playlist ->
            playlist.songs.forEach { song ->
                downloadMusicFileFromUrl(
                    song.url,
                    song.name,
                    getApplication<Application>().applicationContext,
                    playlist.name
                ).join()
                song.playlist = playlist.name
                song.pathToFile = foldersPaths[playlist.name] + "/${song.name}"
            }
        }
        Timber.i("my log end downloadAllMusic")
    }

    // Download ONE song from URL and write folderPath
    private fun downloadMusicFileFromUrl(
        urlString: String, fileName: String, context: Context, playlist: String
    ): Job {
        Timber.i("my log in download $fileName")
        return viewModelScope.launch(Dispatchers.IO) {
            val directoryString = context.filesDir.absolutePath + "/$playlist/"
            val directory = File(directoryString)

            if (!foldersPaths.containsKey(playlist)) {
                foldersPathsPrivate[playlist] = directoryString
            }

            if (File(directory, fileName).exists()) return@launch

            if (!directory.exists()) {
                Timber.i("my log : make dir $directory")
                directory.mkdir()
            }

            Timber.i("my log in coroutine")
            val url = URL(urlString)
            url.openConnection().connect()

            val inputStream = BufferedInputStream(url.openStream())


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