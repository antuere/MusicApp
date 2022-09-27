package com.example.musicapp.database.musicProfileEntity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.musicapp.database.musicProfileEntity.ScheduleEntity
import com.example.musicapp.domain.MusicProfile


@Entity
data class MusicProfileEntity(

    @PrimaryKey
    val id: Int,

    val name: String,

    @Embedded
    val schedule: ScheduleEntity,
    )

