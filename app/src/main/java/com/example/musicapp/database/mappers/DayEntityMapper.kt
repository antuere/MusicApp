package com.example.musicapp.database.mappers

import com.example.musicapp.database.musicProfileEntity.DayEntity
import com.example.musicapp.domain.Day
import com.example.musicapp.domain.util.DomainMapper

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