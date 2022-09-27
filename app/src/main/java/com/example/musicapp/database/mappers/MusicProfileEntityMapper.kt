package com.example.musicapp.database.mappers

import com.example.musicapp.database.musicProfileEntity.MusicProfileEntity
import com.example.musicapp.domain.MusicProfile
import com.example.musicapp.domain.util.DomainMapper

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