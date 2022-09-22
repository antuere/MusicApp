package com.example.musicapp

import android.content.Context
import android.net.Uri
import com.example.musicapp.network.musicProfile.MusicProfile
import com.example.musicapp.network.musicProfile.Playlist
import com.example.musicapp.network.musicProfile.TimeZone
import com.example.musicapp.screens.titleFragment.foldersPaths
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.File
import java.util.*
import kotlin.concurrent.timerTask

object MyPlayer : Player.Listener {

    @Volatile
    private var player: ExoPlayer? = null

    @Volatile
    private var playerExtra: ExoPlayer? = null

    private lateinit var playlists: List<Playlist>

    private var durationSet = false
    private var doCrossFade = false

    fun getInstanceMain(context: Context): ExoPlayer {
        var myPlayer = player
        if (myPlayer == null) {
            synchronized(this) {
                if (myPlayer == null) {
                    myPlayer = ExoPlayer.Builder(context).build()
                    player = myPlayer

                }
            }
        }
        return myPlayer!!
    }

    fun getInstanceExtra(context: Context): ExoPlayer {
        var myPlayer = playerExtra
        if (myPlayer == null) {
            synchronized(this) {
                if (myPlayer == null) {
                    myPlayer = ExoPlayer.Builder(context).build()
                    playerExtra = myPlayer
                }
            }
        }
        return myPlayer!!
    }


    fun setScheduleForPlayer(profile: MusicProfile) {

        val days = profile.schedule.days
        lateinit var timezones: List<TimeZone>
        lateinit var calendar: Calendar
        playlists = profile.schedule.playlists

        player!!.addListener(this)
        playerExtra!!.addListener(this)

        player!!.repeatMode = Player.REPEAT_MODE_ALL
        playerExtra!!.repeatMode = Player.REPEAT_MODE_ALL

//        player!!.shuffleModeEnabled = true
//        playerExtra!!.shuffleModeEnabled = true


        days.forEach { day ->
            calendar = Calendar.getInstance()
            val currentDayOnCalendar = calendar.get(Calendar.DAY_OF_WEEK)
            val dayFromProfile = convertDayOfWeekToNumber(day.day.uppercase(), false)

            if (currentDayOnCalendar == dayFromProfile) {
                Timber.i("error: in the if statement")
                timezones = day.timeZones
                timezones.forEach { timeZone ->
                    setTimer(timeZone, calendar)
                }
            }
        }

    }

    private fun setTimer(timeZone: TimeZone, calendar: Calendar) {

        val hoursStart = timeZone.from.substringBefore(":").toInt()
        val minutesStart = timeZone.from.substringAfter(":").toInt()

        val hoursEnd = timeZone.to.substringBefore(":").toInt()
        val minutesEnd = timeZone.to.substringAfter(":").toInt()

        calendar.set(Calendar.HOUR_OF_DAY, hoursStart)
        calendar.set(Calendar.MINUTE, minutesStart)
        calendar.set(Calendar.SECOND, 3)
        val timerStart = Timer()
        val timerTaskStart = timerTask {
            startPlay(timeZone)
        }
        timerStart.schedule(timerTaskStart, calendar.time)


        calendar.set(Calendar.HOUR_OF_DAY, hoursEnd)
        calendar.set(Calendar.MINUTE, minutesEnd)
        calendar.set(Calendar.SECOND, 0)
        val timerEnd = Timer()
        val timerTaskEnd = timerTask {
            stopPlay(timeZone)
        }

        timerEnd.schedule(timerTaskEnd, calendar.time)
    }

    private fun startPlay(timeZone: TimeZone) {
        val scope = CoroutineScope(Dispatchers.Main)

        scope.launch {

            playerExtra!!.clearMediaItems()
            player!!.clearMediaItems()
            timeZone.playlistsOfZone.forEach { playlistsZone ->
                val playlist = playlistsZone.getPlaylist(playlists)
                playlist.songs.forEach {
                    val path = foldersPaths[playlist.name] + "/${it.name}"
//                    if (it.checkMD5(path)) {
//                        val uri = Uri.fromFile(File(path))
//                        val mediaItem = MediaItem.fromUri(uri)
//
//                        player!!.addMediaItem(mediaItem)
//                        playerExtra!!.addMediaItem(mediaItem)
//
//
//                    }

                    val uri = Uri.fromFile(File(path))
                    val mediaItem = MediaItem.fromUri(uri)

                    val song = mediaItem.mediaMetadata.buildUpon()
                        .setTitle("${playlist.name} - ${it.name}").build()

                    val resultSong = mediaItem.buildUpon().setMediaMetadata(song).build()

                    player!!.addMediaItem(resultSong)
                    playerExtra!!.addMediaItem(resultSong)

                }
            }

            player!!.prepare()
            playerExtra!!.prepare()
            player!!.play()

            playerExtra!!.volume = 0F

        }
    }

    private fun stopPlay(timeZone: TimeZone) {

        Timber.i("my log : enter in stopPlay for $timeZone")
        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {

            player!!.stop()
            playerExtra!!.stop()

            player!!.volume = 1F
            playerExtra!!.volume = 1F

            durationSet = false
            doCrossFade = false

            playerExtra!!.clearMediaItems()
            player!!.clearMediaItems()

//            timeZone.playlistsOfZone.forEach {
//
//                val firstPlayerItems = player!!.mediaItemCount
//                val secondPlayerItems = playerExtra!!.mediaItemCount
//
//                val playlist = it.getPlaylist(playlists)
//
//                for (i in 0 .. firstPlayerItems) {
//
//                    if (i > player!!.mediaItemCount) break
//
//                    val mediaItemFirst = player!!.getMediaItemAt(i)
//
//                    if (mediaItemFirst.mediaMetadata.title.toString().startsWith(playlist.name)) {
//                        player!!.removeMediaItem(i)
//                        player!!.
//                    }
//                }
//
//                for (i in 0 .. secondPlayerItems) {
//                    if (i > playerExtra!!.mediaItemCount) break
//
//                    val mediaItemSecond = playerExtra!!.getMediaItemAt(i)
//
//                    if (mediaItemSecond.mediaMetadata.title.toString().startsWith(playlist.name)) {
//                        playerExtra!!.removeMediaItem(i)
//                    }
//
//                }
//            }
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {

        if (playbackState == ExoPlayer.STATE_READY && !durationSet) {
            Timber.i("my log: enter in onPlaybackStateChanged")
            makeCrossFade(player!!, playerExtra!!)
            durationSet = true
            doCrossFade = true
        }

    }


    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {

        if (player!!.volume == 1F && playerExtra!!.volume == 0.0F && doCrossFade) {
            makeCrossFade(player!!, playerExtra!!)

        } else if (player!!.volume == 0.0F && playerExtra!!.volume == 1F && doCrossFade) {
            makeCrossFade(playerExtra!!, player!!)
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {

        if (!player!!.isPlaying) {

            player!!.seekToNextMediaItem()

        }
        if (!playerExtra!!.isPlaying) {

            playerExtra!!.seekToNextMediaItem()

        }

    }

    private fun makeCrossFade(playerFirst: ExoPlayer, playerSecond: ExoPlayer) {


        val position = playerFirst.currentPosition
        val timeLeft = playerFirst.contentDuration - position

        val delay = timeLeft - 7000
        var delayValue = 0.7F

        val startSong = playerFirst.currentMediaItem!!.mediaMetadata.title

        val timer = Timer()

        val timerTask = timerTask {
            CoroutineScope(Dispatchers.Main).launch {

                if (!playerFirst.isPlaying) {
                    Timber.i("my log: return ;(")
                    return@launch
                }

                val currentSong = playerFirst.currentMediaItem!!.mediaMetadata.title

                if (!playerSecond.isPlaying && currentSong == startSong) {


                    Timber.i("my log: start change volumes")
                    playerFirst.volume = 0.9F
                    delay(350)
                    playerFirst.volume = 0.85F
                    delay(350)

                    playerFirst.volume = 0.8F
                    delay(350)
                    playerFirst.volume = 0.75F
                    delay(350)

                    playerSecond.prepare()
                    playerSecond.play()

                    repeat(14) {
                        playerSecond.volume = 1 - delayValue
                        playerFirst.volume = delayValue
                        delay(350)

                        delayValue -= 0.05F
                    }

                    playerSecond.volume = 1F
                    playerFirst.volume = 0.0F
                    delay(650)


                    playerFirst.stop()

                }
            }
        }
        timer.schedule(timerTask, delay)

    }
}

