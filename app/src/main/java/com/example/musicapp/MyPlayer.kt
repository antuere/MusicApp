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
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.File
import java.sql.Time
import java.util.*
import kotlin.concurrent.timerTask

object MyPlayer : Player.Listener {

    @Volatile
    private var player: ExoPlayer? = null

    @Volatile
    private var playerExtra: ExoPlayer? = null

    private lateinit var playlists: List<Playlist>

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

        days.forEach { day ->
            calendar = Calendar.getInstance()
            val currentDayOnCalendar = calendar.get(Calendar.DAY_OF_WEEK)
            Timber.i("error: currentDayOnCalendar is $currentDayOnCalendar")
            val dayFromProfile = convertDayOfWeekToNumber(day.day.uppercase(), false)
            Timber.i("error: dayFromProfile is $dayFromProfile")

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
        Timber.i("error: enter in the setTimer")

        val hoursStart = timeZone.from.substringBefore(":").toInt()
        val minutesStart = timeZone.from.substringAfter(":").toInt()

        val hoursEnd = timeZone.to.substringBefore(":").toInt()
        val minutesEnd = timeZone.to.substringAfter(":").toInt()


        calendar.set(Calendar.HOUR_OF_DAY, hoursStart)
        calendar.set(Calendar.MINUTE, minutesStart)
        calendar.set(Calendar.SECOND, 1)

        player!!.addListener(this)

        player!!.repeatMode = Player.REPEAT_MODE_ALL
        playerExtra!!.repeatMode = Player.REPEAT_MODE_ALL
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
            stopPlay()
        }
        timerEnd.schedule(timerTaskEnd, calendar.time)

        Timber.i("error: end in the setTimer")

    }

    private fun startPlay(timeZone: TimeZone) {
        val scope = CoroutineScope(Dispatchers.Main)

        scope.launch {
            player!!.clearMediaItems()
            playerExtra!!.clearMediaItems()

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

                    player!!.addMediaItem(mediaItem)
                    playerExtra!!.addMediaItem(mediaItem)
                }
            }


            player!!.shuffleModeEnabled = true

            player!!.prepare()
            playerExtra!!.prepare()
            player!!.play()

            playerExtra!!.volume = 0F
            playerExtra!!.play()
        }
    }

    private fun stopPlay() {
        val scope = CoroutineScope(Dispatchers.Main)

        scope.launch {
            player!!.stop()
            playerExtra!!.stop()

        }
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {

        super.onMediaItemTransition(mediaItem, reason)

        if (reason == Player.DISCONTINUITY_REASON_SEEK || reason == Player.DISCONTINUITY_REASON_AUTO_TRANSITION) {
            if (player!!.volume == 1F) {
                Timber.i("my log: makeCrossFade to mainPlayer")
                makeCrossFade(player!!, playerExtra!!)

            } else if (playerExtra!!.volume == 1F) {
                Timber.i("my log: makeCrossFade to extraPlayer")
                makeCrossFade(playerExtra!!, player!!)

            }
        }

    }


    private fun makeCrossFade(playerFirst: ExoPlayer, playerSecond: ExoPlayer) {

        val delay = playerFirst.duration - 2000
        Timber.i("my log: duration from player ${playerFirst.duration}")

        val timer = Timer()

        val timerTask = timerTask {
            CoroutineScope(Dispatchers.Main).launch {

                playerFirst.volume = 0.9F
                delay(200)

                playerFirst.volume = 0.8F
                delay(200)

                playerFirst.volume = 0.7F
                delay(200)

                playerFirst.volume = 0.6F
                delay(200)

                playerFirst.volume = 0.5F
                delay(200)

                playerFirst.volume = 0.4F
                delay(200)

                playerFirst.volume = 0.3F
                delay(200)

                playerFirst.volume = 0.2F
                delay(200)

                playerFirst.volume = 0.1F
                delay(200)

                playerFirst.volume = 0.0F
                delay(200)


                playerSecond.volume = 0.2F


                playerSecond.volume = 0.3F
                delay(200)

                playerSecond.volume = 0.4F
                delay(200)

                playerSecond.volume = 0.5F
                delay(200)

                playerSecond.volume = 0.6F
                delay(200)

                playerSecond.volume = 0.7F
                delay(200)

                playerSecond.volume = 0.8F
                delay(200)

                playerSecond.volume = 0.9F

                delay(200)
                playerSecond.volume = 1F

            }
        }
        timer.schedule(timerTask, delay)

    }
}

