package com.example.musicapp.network.musicProfileDto

import com.example.musicapp.database.musicProfileEntity.DayEntity
import com.example.musicapp.domain.Day


data class DayDto(
    val day: String,
    val timeZones: List<TimeZoneDto>
)