package com.example.musicapp.database.musicProfileDB

import com.example.musicapp.domain.Schedule


data class ScheduleDB(

    val days: List<DayDB>,
    val playlists: List<PlaylistDB>

) {
    fun asSchedule() : Schedule {
        return Schedule(
            days = days.map { it.asDay() },
            playlists = playlists.map { it.asPlaylist() }
        )
    }
}