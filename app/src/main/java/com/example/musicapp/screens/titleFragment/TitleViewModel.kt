package com.example.musicapp.screens.titleFragment

import android.app.Application
import android.content.Context
import android.media.metrics.PlaybackSession
import androidx.lifecycle.*
import com.example.musicapp.network.MusicApi
import com.example.musicapp.network.musicProfile.MusicProfile
import com.example.musicapp.network.musicProfile.Playlists
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.BufferedInputStream
import java.lang.Exception
import java.net.URL


class TitleViewModel(val applicationMy: Application) : AndroidViewModel(applicationMy) {


    private var _profile = MutableLiveData<MusicProfile>()
    val profile: LiveData<MusicProfile>
        get() = _profile


    private var _playlists = MutableLiveData<List<Playlists>>()
    val playlists: LiveData<List<Playlists>>
        get() = _playlists

    private val _showError = MutableLiveData<String>()
    val showError: LiveData<String>
        get() = _showError

    init {
        getResponseFromServer()

    }

    private fun getResponseFromServer() {
        Timber.i("my log we in the getResponseServer")
        viewModelScope.launch {
            try {
                _profile.value = MusicApi.retrofitService.getJSON()

            } catch (e: Exception) {
                _showError.value = e.message
            }
        }
    }


    fun getPlaylist() {
        _playlists.value = _profile.value!!.schedule.playlists
    }


    fun downloadMusicFile() {
        val url = _playlists.value!!.first().files.first().url
        val fileName = _playlists.value!!.first().files.first().name

        Timber.i("my log url is $url")
        Timber.i("my log fileName is $fileName")

    }

    private fun downloadMusicFileFromUrl(urlString: String, fileName: String, context: Context) {
        Timber.i("my log in download")

        viewModelScope.launch(Dispatchers.IO) {
            Timber.i("my log in coroutine")
            val url = URL(urlString)
            val connection = url.openConnection()

            connection.connect()

            val inputStream = BufferedInputStream(url.openStream())

            val outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            val data = ByteArray(2048)
            var count = inputStream.read(data)
            var total = count

            while (count != -1) {
                Timber.i("my log in cicle")
                outputStream.write(data, 0, count)
                count = inputStream.read(data)
                total += count
            }
            Timber.i("my log save complete")
            outputStream.flush()
            outputStream.close()
            inputStream.close()
        }

    }

}