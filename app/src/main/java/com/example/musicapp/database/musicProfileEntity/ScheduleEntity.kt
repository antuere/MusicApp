package com.example.musicapp.database.musicProfileEntity


data class ScheduleEntity(

    val days: List<DayEntity>,
    val playlists: List<PlaylistEntity>

)
