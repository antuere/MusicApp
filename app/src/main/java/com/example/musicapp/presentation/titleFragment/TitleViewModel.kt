package com.example.musicapp.presentation.titleFragment

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.musicapp.MyPlayer
import com.example.musicapp.database.getDataBase
import com.example.musicapp.domain.usecase.GetMusicProfileUseCase
import com.example.musicapp.domain.usecase.UpdateMusicProfileUseCase
import com.example.musicapp.repository.MusicProfileRepositoryImpl
import com.example.musicapp.util.PlaylistItem
import com.google.android.exoplayer2.ExoPlayer
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.*
import java.lang.Exception
import java.net.URL

private var playlistItemsPrivate = mutableMapOf<String, List<PlaylistItem>>()
val playlistItems: Map<String, List<PlaylistItem>>
    get() = playlistItemsPrivate


class TitleViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDataBase(application)

    private val musicProfileRepository = MusicProfileRepositoryImpl(database, "Test Profile")

    private val getMusicProfileUseCase = GetMusicProfileUseCase(musicProfileRepository)
    private val updateMusicProfileUseCase = UpdateMusicProfileUseCase(musicProfileRepository)

    private var foldersPaths = mutableMapOf<String, String>()

    private var _player = MutableLiveData<ExoPlayer>()
    val player: LiveData<ExoPlayer>
        get() = _player

    private var _playerExtra = MutableLiveData<ExoPlayer>()
    val playerExtra: LiveData<ExoPlayer>
        get() = _playerExtra

    private var _showError = MutableLiveData<String?>()
    val showError: LiveData<String?>
        get() = _showError

    private val _renderUI = MutableLiveData(false)
    val renderUI: LiveData<Boolean>
        get() = _renderUI


    init {
        refreshProfileFromNetwork()
    }

    var profile = getMusicProfileUseCase.invoke()


    /*Made its once on start app:
    * 1)get response from server
    * 1.1) if get error from retrofit, then show message about offline mode
    * 2)download songs in the phone
    * 3)after download songs, set schedule for player
    * */
    private fun refreshProfileFromNetwork() {
        viewModelScope.launch {
            try {
                Timber.i("timer start")

                updateMusicProfileUseCase.invoke()

                delay(25)

                profile = getMusicProfileUseCase.invoke()


            } catch (e: Exception) {
                _showError.value = "Offline mode"
                _showError.value = null
            }

            if (profile.value != null) {
                downloadSongs()
                initExoPlayer()
            }

        }
    }

    //Download ALL songs from profile
    private suspend fun downloadSongs() {
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
    }

    //Initial ExoPlayers
    private fun initExoPlayer() {
        profile.value!!.schedule.days.forEach { day ->
            val items = mutableListOf<PlaylistItem>()
            day.timeZones.forEach { timeZone ->
                timeZone.playlistsOfZone.forEach { playlistsZone ->
                    var showError = false
                    val playlist = playlistsZone.getPlaylist(profile.value!!.schedule.playlists)
                    playlist.songs.forEach {
                        if (!it.checkMD5()) showError = true
                    }
                    val item = PlaylistItem(
                        timeZone.from,
                        timeZone.to,
                        playlist,
                        showError
                    )
                    items.add(item)
                    playlistItemsPrivate[day.day] = items.sorted()
                }
            }
        }

        _renderUI.value = true

        _player.value =
            MyPlayer.getInstanceMain(getApplication<Application>().applicationContext)
        _playerExtra.value =
            MyPlayer.getInstanceExtra(getApplication<Application>().applicationContext)

        Timber.i("timer render start")

        MyPlayer.setScheduleForPlayer(profile.value!!)

        Timber.i("timer end")
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
                foldersPaths[playlist] = directoryString
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

            val data = ByteArray(2048)
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