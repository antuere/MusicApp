package com.example.musicapp.database.musicProfileEntity

import com.example.musicapp.domain.Day


data class DayEntity(

    val day: String,

    val timeZones: List<TimeZoneEntity>

)  {

}