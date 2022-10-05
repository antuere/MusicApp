package com.antuere.musicapp.presentation.playerFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.antuere.musicapp.util.MyPlayer
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(myPlayer: MyPlayer) : ViewModel(),
    Player.Listener {

    private var _mainPlayer = MutableLiveData<ExoPlayer>()
    val mainPlayer: LiveData<ExoPlayer>
        get() = _mainPlayer

    private var _extraPlayer = MutableLiveData<ExoPlayer>()
    val extraPlayer: LiveData<ExoPlayer>
        get() = _extraPlayer

    private var _showMain = MutableLiveData(true)
    val showMain: LiveData<Boolean>
        get() = _showMain

    private var _changeTitle = MutableLiveData(true)
    val changeTitle: LiveData<Boolean>
        get() = _changeTitle

    init {
        _mainPlayer.value =
            myPlayer.player
        _extraPlayer.value =
            myPlayer.playerExtra

        _mainPlayer.value!!.addListener(this)
        _extraPlayer.value!!.addListener(this)

        chooseCurrentPlayer()
        chooseCurrentTitle()
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {

        chooseCurrentTitle()

    }

    override fun onVolumeChanged(volume: Float) {

        chooseCurrentPlayer()

    }

    private fun chooseCurrentPlayer() {
        if (_mainPlayer.value!!.volume > 0.65F) {

            _showMain.value = true

        } else if (_extraPlayer.value!!.volume > 0.65F) {

            _showMain.value = false

        }
    }

    private fun chooseCurrentTitle() {
        if (!_mainPlayer.value!!.isPlaying && !_extraPlayer.value!!.isPlaying) {

            _changeTitle.value = true

        } else if (!_mainPlayer.value!!.isPlaying && _extraPlayer.value!!.isPlaying) {

            _changeTitle.value = false
        }
    }
}