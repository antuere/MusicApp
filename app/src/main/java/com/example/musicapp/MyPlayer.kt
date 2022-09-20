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
        playerExtra!!.addListener(this)

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

                    val song = mediaItem.mediaMetadata.buildUpon()
                        .setTitle("${playlist.name} - ${it.name}").build()

                    val resultSong = mediaItem.buildUpon().setMediaMetadata(song).build()

                    player!!.addMediaItem(resultSong)
                    playerExtra!!.addMediaItem(resultSong)
                }
            }


            player!!.shuffleModeEnabled = true
            playerExtra!!.shuffleModeEnabled = true

            player!!.prepare()
            playerExtra!!.prepare()
            player!!.play()

            playerExtra!!.volume = 0F
            playerExtra!!.seekToNextMediaItem()

        }
    }

    private fun stopPlay() {
        val scope = CoroutineScope(Dispatchers.Main)

        scope.launch {
            player!!.stop()
            playerExtra!!.stop()

        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {

        if (playbackState == ExoPlayer.STATE_READY && !durationSet) {
            Timber.i("my log: makeCrossFade to mainPlayer first time, playing ${player!!.mediaMetadata.title}")
            makeCrossFade(player!!, playerExtra!!)
            durationSet = true
            doCrossFade = true
        }

    }


    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {

        Timber.i("my log: mediaDataChanged")

        if (player!!.volume == 1F && doCrossFade) {
            Timber.i("my log: makeCrossFade to mainPlayer playing ${player!!.mediaMetadata.title}")
            makeCrossFade(player!!, playerExtra!!)

        } else if (playerExtra!!.volume == 1F && doCrossFade) {
            Timber.i("my log: makeCrossFade to extraPlayer playing ${playerExtra!!.mediaMetadata.title}")
            makeCrossFade(playerExtra!!, player!!)
        }
    }

    private fun makeCrossFade(playerFirst: ExoPlayer, playerSecond: ExoPlayer) {

        Timber.i("my log: makeCrossFade now is playing ${playerFirst.mediaMetadata.title}")

        val position = playerFirst.currentPosition
        val timeLeft = playerFirst.contentDuration - position

        val delay = timeLeft - 7000
        var delayValue = 0.7F

        Timber.i("my log: duration from player ${playerFirst.duration}")

        val timer = Timer()

        Timber.i("my log: before timer task")
        val timerTask = timerTask {
            CoroutineScope(Dispatchers.Main).launch {
                Timber.i("my log: enter timer task")
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
                playerFirst.seekToNextMediaItem()

                Timber.i("my log: exit timer task")

            }
        }
        timer.schedule(timerTask, delay)

    }
}

