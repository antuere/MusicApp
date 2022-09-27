package com.example.musicapp.network.mappers

import com.example.musicapp.database.musicProfileEntity.PlaylistsZoneEntity
import com.example.musicapp.database.util.EntityMapper
import com.example.musicapp.network.musicProfileDto.PlaylistsZoneDto

class PlaylistZoneDtoMapper : EntityMapper<PlaylistsZoneDto, PlaylistsZoneEntity> {

    override fun mapToEntity(t: PlaylistsZoneDto): PlaylistsZoneEntity {
        return PlaylistsZoneEntity(t.playlistId, t.proportion)
    }

    override fun mapFromEntity(entity: PlaylistsZoneEntity): PlaylistsZoneDto {
        return PlaylistsZoneDto(entity.playlistId, entity.proportion)
    }
}