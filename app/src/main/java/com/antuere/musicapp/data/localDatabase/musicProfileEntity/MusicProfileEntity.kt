package com.antuere.musicapp.data.localDatabase.musicProfileEntity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class MusicProfileEntity(

    @PrimaryKey
    val id: Int,

    val name: String,

    @Embedded
    val schedule: ScheduleEntity,
    )

