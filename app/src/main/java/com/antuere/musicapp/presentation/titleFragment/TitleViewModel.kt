package com.antuere.musicapp.presentation.titleFragment

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.antuere.musicapp.util.MyPlayer
import com.antuere.domain.usecase.GetMusicProfileUseCase
import com.antuere.domain.usecase.UpdateMusicProfileUseCase
import com.antuere.musicapp.util.MyMusicDownloader
import com.antuere.musicapp.util.PlaylistItem
import com.google.android.exoplayer2.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.*
import java.lang.Exception
import java.net.URL
import javax.inject.Inject


private var playlistItemsPrivate = mutableMapOf<String, List<PlaylistItem>>()
val playlistItems: Map<String, List<PlaylistItem>>
    get() = playlistItemsPrivate

@HiltViewModel
class TitleViewModel @Inject constructor(
    private val myPlayer: MyPlayer,
    private val myMusicDownloader: MyMusicDownloader,
    getMusicProfileUseCase: GetMusicProfileUseCase,
    private val updateMusicProfileUseCase: UpdateMusicProfileUseCase

) : ViewModel() {


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

    var profile = getMusicProfileUseCase.invoke().asLiveData(Dispatchers.Main)


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
                delay(200)

            } catch (e: Exception) {
                _showError.value = "Offline mode"
                _showError.value = null
            }

            if (profile.value != null) {

                myMusicDownloader.downloadSongs(profile.value!!)

                initExoPlayer()
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
        Timber.i("timer render start")

        _player.value = myPlayer.player
        _playerExtra.value = myPlayer.playerExtra

        myPlayer.setScheduleForPlayer(profile.value!!)

        Timber.i("timer end")
        Timber.i("my log end downloadAllMusic")
    }


}