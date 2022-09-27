package com.example.musicapp.network.mappers

import com.example.musicapp.database.musicProfileEntity.ScheduleEntity
import com.example.musicapp.database.util.EntityMapper
import com.example.musicapp.network.musicProfileDto.ScheduleDto

class ScheduleDtoMapper(
    private val dayDtoMapper: DayDtoMapper,
    private val playlistDtoMapper: PlaylistDtoMapper
) : EntityMapper<ScheduleDto, ScheduleEntity> {


    override fun mapToEntity(t: ScheduleDto): ScheduleEntity {
        return ScheduleEntity(
            days = t.days.map { dayDtoMapper.mapToEntity(it) },
            playlists = t.playlists.map { playlistDtoMapper.mapToEntity(it) }
        )
    }

    override fun mapFromEntity(entity: ScheduleEntity): ScheduleDto {
        return ScheduleDto(
            days = entity.days.map { dayDtoMapper.mapFromEntity(it) },
            playlists = entity.playlists.map { playlistDtoMapper.mapFromEntity(it) }
        )
    }
}