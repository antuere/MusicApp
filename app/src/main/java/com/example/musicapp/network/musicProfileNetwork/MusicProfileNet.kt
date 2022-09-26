package com.example.musicapp.network.musicProfileNetwork

import com.example.musicapp.database.MusicProfileDB
import com.example.musicapp.domain.MusicProfile
import com.squareup.moshi.Json


data class MusicProfileNet(
    val id: Int,

    val name: String,

    val schedule: ScheduleNet,

) {

    fun asMusicProfile(): MusicProfile {
        return MusicProfile(
            id = id,
            name = name,
            schedule = schedule.asSchedule(),
        )
    }

    fun asMusicProfileDB(): MusicProfileDB {
        return MusicProfileDB(
            id = id,
            name = name,
            schedule = schedule.asScheduleDB()
        )
    }

}