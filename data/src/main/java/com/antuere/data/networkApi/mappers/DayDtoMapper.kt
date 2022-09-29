package com.antuere.data.networkApi.mappers

import com.antuere.data.localDatabase.musicProfileEntity.DayEntity
import com.antuere.data.localDatabase.util.EntityMapper
import com.antuere.data.networkApi.musicProfileDto.DayDto

class DayDtoMapper(private val timeZoneDtoMapper: TimeZoneDtoMapper) :
    EntityMapper<DayDto, DayEntity> {


    override fun mapToEntity(t: DayDto): DayEntity {
        return DayEntity(
            t.day,
            t.timeZones.map { timeZoneDtoMapper.mapToEntity(it) }
        )
    }

    override fun mapFromEntity(entity: DayEntity): DayDto {
        return DayDto(
            entity.day,
            entity.timeZones.map { timeZoneDtoMapper.mapFromEntity(it) }
        )
    }
}