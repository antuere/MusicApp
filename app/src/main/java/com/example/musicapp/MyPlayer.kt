package com.example.musicapp

import android.content.Context
import android.net.Uri
import com.example.musicapp.network.musicProfile.MusicProfile
import com.example.musicapp.network.musicProfile.Playlist
import com.example.musicapp.network.musicProfile.TimeZone
import com.example.musicapp.screens.titleFragment.foldersPaths
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import java.io.File
import java.util.*
import kotlin.concurrent.timerTask

object MyPlayer : Player.Listener {

    @Volatile
    private var player: ExoPlayer? = null
    private lateinit var playlists: List<Playlist>

    fun getInstance(context: Context): ExoPlayer {
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

    fun setScheduleForPlayer(profile: MusicProfile) {

        val days = profile.schedule.days
        lateinit var timezones: List<TimeZone>
        lateinit var calendar: Calendar
        playlists = profile.schedule.playlists

        days.forEach { day ->
            calendar = Calendar.getInstance()
            val currentDayOnCalendar = calendar.get(Calendar.DAY_OF_WEEK)
            val dayFromProfile = convertDayOfWeekToNumber(day.toString().uppercase(), false)

            if (currentDayOnCalendar == dayFromProfile) {
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
        calendar.set(Calendar.SECOND, 1)

        player!!.addListener(this)
        player!!.repeatMode = Player.REPEAT_MODE_ALL
        val timerStart = Timer()
        val timerTaskStart = timerTask {
            player!!.clearMediaItems()

            timeZone.playlistsOfZone.forEach { playlistsZone ->
                val playlist = playlistsZone.getPlaylist(playlists)
                playlist.songs.forEach {
                    val uri = Uri.fromFile(File(foldersPaths[playlist.name]!! + "/${it.name}"))
                    val mediaItem = MediaItem.fromUri(uri)
                    player!!.addMediaItem(mediaItem)
                }
            }
            player!!.shuffleModeEnabled = true
            player!!.prepare()
            player!!.play()

        }
        timerStart.schedule(timerTaskStart, calendar.time)

        calendar.set(Calendar.HOUR_OF_DAY, hoursEnd)
        calendar.set(Calendar.MINUTE, minutesEnd)
        calendar.set(Calendar.SECOND, 0)


        val timerEnd = Timer()
        val timerTaskEnd = timerTask {
            player!!.stop()
        }
        timerEnd.schedule(timerTaskEnd, calendar.time)


    }
}

