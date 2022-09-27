package com.example.musicapp.database.mappers

import com.example.musicapp.database.musicProfileEntity.PlaylistsZoneEntity
import com.example.musicapp.domain.PlaylistsZone
import com.example.musicapp.domain.util.DomainMapper

class PlaylistZoneEntityMapper : DomainMapper<PlaylistsZoneEntity, PlaylistsZone> {

    override fun mapToDomainModel(model: PlaylistsZoneEntity): PlaylistsZone {
        return PlaylistsZone(model.playlistId, model.proportion)
    }

    override fun mapFromDomainModel(domainModel: PlaylistsZone): PlaylistsZoneEntity {
        return PlaylistsZoneEntity(domainModel.playlistId, domainModel.proportion)
    }
}