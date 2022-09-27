package com.example.musicapp.database.mappers

import com.example.musicapp.database.musicProfileEntity.ScheduleEntity
import com.example.musicapp.domain.Schedule
import com.example.musicapp.domain.util.DomainMapper

class ScheduleEntityMapper(
    private val dayEntityMapper: DayEntityMapper,
    private val playlistEntityMapper: PlaylistEntityMapper
) : DomainMapper<ScheduleEntity, Schedule> {

    override fun mapToDomainModel(model: ScheduleEntity): Schedule {
        return Schedule(
            days = model.days.map { dayEntityMapper.mapToDomainModel(it) },
            playlists = model.playlists.map { playlistEntityMapper.mapToDomainModel(it) }
        )
    }

    override fun mapFromDomainModel(domainModel: Schedule): ScheduleEntity {
        return ScheduleEntity(
            days = domainModel.days.map { dayEntityMapper.mapFromDomainModel(it) },
            playlists = domainModel.playlists.map { playlistEntityMapper.mapFromDomainModel(it) }
        )
    }
}