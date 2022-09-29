package com.antuere.musicapp.networkApi.mappers

import com.antuere.musicapp.data.localDatabase.musicProfileEntity.TimeZoneEntity
import com.antuere.musicapp.data.localDatabase.util.EntityMapper
import com.antuere.musicapp.networkApi.musicProfileDto.TimeZoneDto

class TimeZoneDtoMapper(private val playlistZoneDtoMapper: PlaylistZoneDtoMapper) :
    EntityMapper<TimeZoneDto, TimeZoneEntity> {

    override fun mapToEntity(t: TimeZoneDto): TimeZoneEntity {
        return TimeZoneEntity(
            t.from,
            playlistsOfZone = t.playlistsOfZone.map {
                playlistZoneDtoMapper.mapToEntity(it)
            },
            t.to
        )
    }

    override fun mapFromEntity(entity: TimeZoneEntity): TimeZoneDto {
        return TimeZoneDto(
            entity.from,
            playlistsOfZone = entity.playlistsOfZone.map {
                playlistZoneDtoMapper.mapFromEntity(it)
            },
            entity.to
        )
    }
}