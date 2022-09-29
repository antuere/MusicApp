package com.antuere.data.localDatabase.mappers

import com.antuere.data.localDatabase.musicProfileEntity.MusicProfileEntity
import com.antuere.domain.musicProfile.MusicProfile
import com.antuere.domain.util.DomainMapper

class MusicProfileEntityMapper(private val scheduleDtoMapper: ScheduleEntityMapper) :
    DomainMapper<MusicProfileEntity, MusicProfile> {

    override fun mapToDomainModel(model: MusicProfileEntity): MusicProfile {
        return MusicProfile(
            id = model.id,
            name = model.name,
            schedule = scheduleDtoMapper.mapToDomainModel(model.schedule)
        )
    }

    override fun mapFromDomainModel(domainModel: MusicProfile): MusicProfileEntity {

        return MusicProfileEntity(
            id = domainModel.id,
            name = domainModel.name,
            schedule = scheduleDtoMapper.mapFromDomainModel(domainModel.schedule)
        )
    }


}