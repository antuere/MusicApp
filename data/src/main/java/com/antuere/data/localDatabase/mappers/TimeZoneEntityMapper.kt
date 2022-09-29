package com.antuere.data.localDatabase.mappers

import com.antuere.data.localDatabase.musicProfileEntity.TimeZoneEntity
import com.antuere.domain.musicProfile.TimeZone
import com.antuere.domain.util.DomainMapper

class TimeZoneEntityMapper(private val playlistZoneEntityMapper: PlaylistZoneEntityMapper) :
    DomainMapper<TimeZoneEntity, TimeZone> {

    override fun mapToDomainModel(model: TimeZoneEntity): TimeZone {
        return TimeZone(
            model.from,
            playlistsOfZone = model.playlistsOfZone.map {
                playlistZoneEntityMapper.mapToDomainModel(it)
            },
            model.to
        )
    }

    override fun mapFromDomainModel(domainModel: TimeZone): TimeZoneEntity {
        return TimeZoneEntity(
            domainModel.from,
            playlistsOfZone = domainModel.playlistsOfZone.map {
                playlistZoneEntityMapper.mapFromDomainModel(it)
            },
            domainModel.to
        )
    }
}