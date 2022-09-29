package com.antuere.data.networkApi.mappers

import com.antuere.data.localDatabase.musicProfileEntity.ScheduleEntity
import com.antuere.data.localDatabase.util.EntityMapper
import com.antuere.data.networkApi.musicProfileDto.ScheduleDto

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