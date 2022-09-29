package com.antuere.musicapp.data.localDatabase.mappers

import com.antuere.musicapp.data.localDatabase.musicProfileEntity.PlaylistsZoneEntity
import com.antuere.musicapp.domain.musicProfile.PlaylistsZone
import com.antuere.musicapp.domain.util.DomainMapper

class PlaylistZoneEntityMapper : DomainMapper<PlaylistsZoneEntity, PlaylistsZone> {

    override fun mapToDomainModel(model: PlaylistsZoneEntity): PlaylistsZone {
        return PlaylistsZone(model.playlistId, model.proportion)
    }

    override fun mapFromDomainModel(domainModel: PlaylistsZone): PlaylistsZoneEntity {
        return PlaylistsZoneEntity(domainModel.playlistId, domainModel.proportion)
    }
}