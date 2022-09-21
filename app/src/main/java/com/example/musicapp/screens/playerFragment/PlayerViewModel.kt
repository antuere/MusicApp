package com.example.musicapp.screens.playerFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicapp.MyPlayer
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player

class PlayerViewModel(application: Application) : AndroidViewModel(application), Player.Listener {

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
            MyPlayer.getInstanceMain(getApplication<Application>().applicationContext)
        _extraPlayer.value =
            MyPlayer.getInstanceExtra(getApplication<Application>().applicationContext)

        _mainPlayer.value!!.addListener(this)
        _extraPlayer.value!!.addListener(this)
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {

        if (!_mainPlayer.value!!.isPlaying && !_extraPlayer.value!!.isPlaying){

            _changeTitle.value = true

        } else if (!_mainPlayer.value!!.isPlaying && _extraPlayer.value!!.isPlaying ) {

            _changeTitle.value = false

        }
    }

    override fun onVolumeChanged(volume: Float) {

        if ( _mainPlayer.value!!.volume > 0.65F   ){

            _showMain.value = true

        } else if ( _extraPlayer.value!!.volume > 0.65F ) {

            _showMain.value = false

        }


    }
}