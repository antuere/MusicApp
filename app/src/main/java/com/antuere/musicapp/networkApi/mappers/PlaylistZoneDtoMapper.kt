package com.antuere.musicapp.networkApi.mappers

import com.antuere.musicapp.data.localDatabase.musicProfileEntity.PlaylistsZoneEntity
import com.antuere.musicapp.data.localDatabase.util.EntityMapper
import com.antuere.musicapp.networkApi.musicProfileDto.PlaylistsZoneDto

class PlaylistZoneDtoMapper : EntityMapper<PlaylistsZoneDto, PlaylistsZoneEntity> {

    override fun mapToEntity(t: PlaylistsZoneDto): PlaylistsZoneEntity {
        return PlaylistsZoneEntity(t.playlistId, t.proportion)
    }

    override fun mapFromEntity(entity: PlaylistsZoneEntity): PlaylistsZoneDto {
        return PlaylistsZoneDto(entity.playlistId, entity.proportion)
    }
}