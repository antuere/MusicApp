package com.example.musicapp.network.musicProfileDto

import com.example.musicapp.database.musicProfileEntity.ScheduleEntity
import com.example.musicapp.domain.Schedule


data class ScheduleDto(

    val days: List<DayDto>,

    val playlists: List<PlaylistDto>

)

