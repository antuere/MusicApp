package com.antuere.data.localDatabase.mappers

import com.antuere.data.localDatabase.musicProfileEntity.DayEntity
import com.antuere.domain.musicProfile.Day
import com.antuere.domain.util.DomainMapper

class DayEntityMapper(private val timeZoneEntityMapper: TimeZoneEntityMapper) :
    DomainMapper<DayEntity, Day> {

    override fun mapToDomainModel(model: DayEntity): Day {
        return Day(
            model.day,
            model.timeZones.map { timeZoneEntityMapper.mapToDomainModel(it) }
        )
    }

    override fun mapFromDomainModel(domainModel: Day): DayEntity {
        return DayEntity(
            domainModel.day,
            domainModel.timeZones.map { timeZoneEntityMapper.mapFromDomainModel(it) }
        )
    }
}