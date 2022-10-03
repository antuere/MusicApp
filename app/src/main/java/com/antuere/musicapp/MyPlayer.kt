package com.antuere.musicapp

import android.content.Context
import android.net.Uri
import com.antuere.domain.musicProfile.TimeZone
import com.antuere.domain.musicProfile.MusicProfile
import com.antuere.domain.musicProfile.Playlist
import com.antuere.domain.musicProfile.PlaylistsZone
import com.antuere.domain.usecase.GetSongsForPlayerUseCase
import com.antuere.domain.util.convertDayOfWeekToNumber
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.*
import timber.log.Timber
import java.io.File
import java.util.*
import kotlin.concurrent.timerTask


/* Singleton class for ExoPlayer, working with schedule.
For crossFade effect we created 2 instances of ExoPlayer*/

object MyPlayer : Player.Listener {

    @Volatile
    private var player: ExoPlayer? = null

    @Volatile
    private var playerExtra: ExoPlayer? = null

    private lateinit var playlistsDownload: List<Playlist>

    lateinit var playlistsRequired: MutableList<PlaylistsZone>

    private var durationSet = false
    private var doCrossFade = false
    private var isAddNew = true

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
        playlistsDownload = profile.schedule.playlists

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

            addSongsToPlayer()
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

    private fun addSongsToPlayer() {

        val getSongsForPlayerUseCase =
            GetSongsForPlayerUseCase(playlistsRequired, playlistsDownload)

        val songs = getSongsForPlayerUseCase.invoke()
        if (songs.isNotEmpty()) {
            songs.forEach {
//                Add check MD5 when run on real device
                if (true) {
                    val uri = Uri.fromFile(File(it.pathToFile))
                    val mediaItem = MediaItem.fromUri(uri)

                    val song = mediaItem.mediaMetadata.buildUpon()
                        .setTitle("${it.playlist} - ${it.name}").build()

                    val resultSong = mediaItem.buildUpon().setMediaMetadata(song).build()

                    player!!.addMediaItem(resultSong)
                    playerExtra!!.addMediaItem(resultSong)
                }
            }
        }
    }


    fun updateSongs() {
        player!!.volume = 1F
        playerExtra!!.volume = 1F

        resetFlags()

        player!!.stop()
        playerExtra!!.stop()

        player!!.clearMediaItems()
        playerExtra!!.clearMediaItems()

        addSongsToPlayer()

        isAddNew = true

        player!!.prepare()
        player!!.play()

        playerExtra!!.volume = 0F
    }

    private fun resetFlags() {
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
            makeCrossFade(player!!, playerExtra!!)

        } else if (player!!.volume == 0.0F && playerExtra!!.volume == 1F && doCrossFade) {
            Timber.i("my log: mediaChange for extra")
            makeCrossFade(playerExtra!!, player!!)
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
                addSongsToPlayer()
            }
            player!!.seekToNextMediaItem()

        }
        if (!playerExtra!!.isPlaying) {
            Timber.i("my log: extra player stop")
            if (!playerExtra!!.hasNextMediaItem()) {
                addSongsToPlayer()
            }
            playerExtra!!.seekToNextMediaItem()

        }

        Timber.i("my log: exit onIsPlayChanged")
    }

    private fun makeCrossFade(playerFirst: ExoPlayer, playerSecond: ExoPlayer) {

        val position = playerFirst.currentPosition
        val timeLeft = playerFirst.contentDuration - position

//        Start crossFade effect 7 seconds before the end of the song
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
                    delay(800)

                    playerFirst.stop()


                }
            }
        }
        timer.schedule(timerTask, delay)
    }
}

