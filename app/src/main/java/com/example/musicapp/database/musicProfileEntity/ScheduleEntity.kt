package com.example.musicapp.database.musicProfileEntity

import com.example.musicapp.domain.Schedule


data class ScheduleEntity(

    val days: List<DayEntity>,
    val playlists: List<PlaylistEntity>

)
