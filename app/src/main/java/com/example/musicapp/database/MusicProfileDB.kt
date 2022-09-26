package com.example.musicapp.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.musicapp.database.musicProfileDB.ScheduleDB
import com.example.musicapp.domain.MusicProfile


@Entity
data class MusicProfileDB(

    @PrimaryKey
    val id: Int,

    val name: String,

    @Embedded
    val schedule: ScheduleDB,


) {


    fun asDomain(): MusicProfile {
        return MusicProfile(
            id = id,
            name = name,
            schedule = schedule.asSchedule(),
        )
    }
}
