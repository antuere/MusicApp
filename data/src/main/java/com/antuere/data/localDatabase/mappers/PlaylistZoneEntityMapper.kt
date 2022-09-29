package com.antuere.data.localDatabase.mappers

import com.antuere.data.localDatabase.musicProfileEntity.PlaylistsZoneEntity
import com.antuere.domain.musicProfile.PlaylistsZone
import com.antuere.domain.util.DomainMapper

class PlaylistZoneEntityMapper : DomainMapper<PlaylistsZoneEntity, PlaylistsZone> {

    override fun mapToDomainModel(model: PlaylistsZoneEntity): PlaylistsZone {
        return PlaylistsZone(model.playlistId, model.proportion)
    }

    override fun mapFromDomainModel(domainModel: PlaylistsZone): PlaylistsZoneEntity {
        return PlaylistsZoneEntity(domainModel.playlistId, domainModel.proportion)
    }
}