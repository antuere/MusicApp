package com.antuere.musicapp.util

import android.content.Context
import androidx.lifecycle.asLiveData
import com.antuere.domain.musicProfile.MusicProfile
import com.antuere.domain.usecase.GetMusicProfileUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.net.URL

class MyMusicDownloader(val context: Context) {

    private var foldersPaths = mutableMapOf<String, String>()

    //Download ALL songs from profile
    suspend fun downloadSongs(profile: MusicProfile) {

        profile.schedule.playlists.forEach { playlist ->
            playlist.songs.forEach { song ->
                downloadMusicFileFromUrl(
                    song.url,
                    song.name,
                    context,
                    playlist.name
                ).join()
                song.playlist = playlist.name
                song.pathToFile = foldersPaths[playlist.name] + "/${song.name}"
            }
        }
    }


    // Download ONE song from URL and write folderPath
    private fun downloadMusicFileFromUrl(
        urlString: String, fileName: String, context: Context, playlist: String
    ): Job {
        Timber.i("my log in download $fileName")
        val scope = CoroutineScope(Dispatchers.Main + Job())
        return scope.launch(Dispatchers.IO) {
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