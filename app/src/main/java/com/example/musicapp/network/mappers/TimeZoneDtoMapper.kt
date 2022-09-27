package com.example.musicapp.network.mappers

import com.example.musicapp.database.musicProfileEntity.TimeZoneEntity
import com.example.musicapp.database.util.EntityMapper
import com.example.musicapp.network.musicProfileDto.TimeZoneDto

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