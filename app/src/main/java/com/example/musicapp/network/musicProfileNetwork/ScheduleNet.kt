package com.example.musicapp.network.musicProfileNetwork

import com.example.musicapp.database.musicProfileDB.ScheduleDB
import com.example.musicapp.domain.Schedule


data class ScheduleNet(

    val days: List<DayNet>,

    val playlists: List<PlaylistNet>

) {
    fun asSchedule(): Schedule {
        return Schedule(
            days = days.map { it.asDay() },
            playlists = playlists.map { it.asPlaylist() }
        )
    }

    fun asScheduleDB(): ScheduleDB {
        return ScheduleDB(
            days = days.map { it.asDayDB() },
            playlists = playlists.map { it.asPlaylistDB() }
        )
    }
}