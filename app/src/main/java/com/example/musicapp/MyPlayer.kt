package com.example.musicapp

import android.content.Context
import com.example.musicapp.domain.*
import com.example.musicapp.domain.TimeZone
import com.example.musicapp.domain.usecase.AddSongsToPlayerUseCase
import com.example.musicapp.domain.usecase.MakeCrossFadeUseCase
import com.example.musicapp.util.convertDayOfWeekToNumber
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*
import kotlin.concurrent.timerTask


/* Singleton class for ExoPlayer, working with schedule.
For crossFade effect we created 2 instances of ExoPlayer*/

object MyPlayer : Player.Listener {

    @Volatile
    private var player: ExoPlayer? = null

    @Volatile
    private var playerExtra: ExoPlayer? = null

    private lateinit var playlistsDownloadPrivate: List<Playlist>
    val playlistsDownload: List<Playlist>
        get() = playlistsDownloadPrivate

    lateinit var playlistsRequired: MutableList<PlaylistsZone>

    private var durationSet = false
    private var doCrossFade = false
    var isAddNew = true

    private val addSongsToPlayerUseCase: AddSongsToPlayerUseCase by lazy {
        AddSongsToPlayerUseCase(player!!, playerExtra!!)
    }

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

/*   Made this once when app start first time or after destroy.
*    Set schedule rules based on profile instance, get days from profile,
*    iterate on days, and when some day from listDays match with current day,
*    we set timers for playlists */

    fun setScheduleForPlayer(profile: MusicProfile) {

        val days = profile.schedule.days
        lateinit var timezones: List<TimeZone>
        lateinit var calendar: Calendar
        playlistsDownloadPrivate = profile.schedule.playlists

        player!!.addListener(this)
        playerExtra!!.addListener(this)

        days.forEach { day ->
            calendar = Calendar.getInstance()
            val currentDayOnCalendar = calendar.get(Calendar.DAY_OF_WEEK)
            val dayFromProfile = convertDayOfWeekToNumber(day.day.uppercase(), false)

            if (currentDayOnCalendar == dayFromProfile) {
                timezones = day.timeZones
                timezones.forEach { timeZone ->
                    setTimer(timeZone, calendar)
                }
            }
        }
    }

/*    Set timer for timezone: time for playlist start and time for playlist end.
*     Because work with Timer should come from MainThread, in TimerTask need use coroutines with
*     dispatcher.Main */

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
            Timber.i("my log : enter in startPlay for $timeZone")
            playerExtra!!.clearMediaItems()
            player!!.clearMediaItems()

            playlistsRequired = mutableListOf()

            timeZone.playlistsOfZone.forEach { playlistsZone ->
                playlistsRequired.add(playlistsZone)
            }

            addSongsToPlayerUseCase.invoke()
            isAddNew = true

            player!!.prepare()
            playerExtra!!.volume = 0F

            Timber.i("my log : exit in startPlay for $timeZone")

        }
    }



    private fun stopPlay(timeZone: TimeZone) {

        Timber.i("my log : enter in stopPlay for $timeZone")
        val scope = CoroutineScope(Dispatchers.Main)

        scope.launch {
            player!!.volume = 1F
            playerExtra!!.volume = 1F

            player!!.stop()
            playerExtra!!.stop()

            resetFlags()

            playerExtra!!.clearMediaItems()
            player!!.clearMediaItems()
            playlistsRequired = mutableListOf()
        }
    }

    fun resetFlags() {
        durationSet = false
        doCrossFade = false
        isAddNew = false
    }

    override fun onPlaybackStateChanged(playbackState: Int) {

        if (playbackState == ExoPlayer.STATE_READY && !durationSet) {
            Timber.i("my log: enter in onPlaybackStateChanged")
            durationSet = true
            doCrossFade = true
        }

    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {

        if (player!!.volume == 1F && playerExtra!!.volume == 0.0F && doCrossFade) {
            Timber.i("my log: mediaChange for main")
            val makeCrossFadeUseCase = MakeCrossFadeUseCase(player!!, playerExtra!!)
            makeCrossFadeUseCase.invoke()

        } else if (player!!.volume == 0.0F && playerExtra!!.volume == 1F && doCrossFade) {
            Timber.i("my log: mediaChange for extra")
            val makeCrossFadeUseCase = MakeCrossFadeUseCase(playerExtra!!, player!!)
            makeCrossFadeUseCase.invoke()
        }

    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {

        Timber.i("my log: enter onIsPlayChanged")

        if (!player!!.isPlaying && !playerExtra!!.isPlaying && isAddNew) {
            Timber.i("my log: all players stop")
            if (player!!.volume > 0) {
                player!!.seekToNextMediaItem()
            } else if (playerExtra!!.volume > 0) {
                playerExtra!!.seekToNextMediaItem()
            }
            return
        }

        if (!player!!.isPlaying) {
            Timber.i("my log: main player stop")
            if (!player!!.hasNextMediaItem()) {
                addSongsToPlayerUseCase.invoke()
            }
            player!!.seekToNextMediaItem()

        }
        if (!playerExtra!!.isPlaying) {
            Timber.i("my log: extra player stop")
            if (!playerExtra!!.hasNextMediaItem()) {
                addSongsToPlayerUseCase.invoke()
            }
            playerExtra!!.seekToNextMediaItem()

        }

        Timber.i("my log: exit onIsPlayChanged")
    }
}

