package com.example.musicapp.screens.titleFragment

import android.app.Application
import android.content.Context
import android.media.metrics.PlaybackSession
import androidx.lifecycle.*
import com.example.musicapp.network.MusicApi
import com.example.musicapp.network.musicProfile.MusicProfile
import com.example.musicapp.network.musicProfile.Playlists
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.BufferedInputStream
import java.lang.Exception
import java.net.URL
import java.nio.channels.Channels


class TitleViewModel(private val applicationMy: Application) : AndroidViewModel(applicationMy) {


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

                _playlists.value = _profile.value!!.schedule.playlists

                downloadMusicFiles()

            } catch (e: Exception) {
                _showError.value = e.message
            }
        }
    }


    private fun downloadMusicFiles() {
        _playlists.value!!.forEach { playlist ->
            playlist.files.forEach { file ->
                downloadMusicFileFromUrl(
                    file.url,
                    file.name,
                    getApplication<Application>().applicationContext
                )
            }
        }

    }

    private fun downloadMusicFileFromUrl(urlString: String, fileName: String, context: Context) {

        Timber.i("my log in download $fileName")

        viewModelScope.launch(Dispatchers.IO) {
            Timber.i("my log in coroutine")
            val url = URL(urlString)

            url.openConnection().connect()

            val inputStream = BufferedInputStream(url.openStream())

            val outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)

            val data = ByteArray(1024)

            var count = inputStream.read(data)

            while (count != -1) {
                outputStream.write(data, 0, count)
                count = inputStream.read(data)
            }

            Timber.i("my log save complete $fileName")
            outputStream.flush()
            outputStream.close()
            inputStream.close()
        }
    }

}