package com.antuere.musicapp.networkApi.mappers

import com.antuere.musicapp.data.localDatabase.musicProfileEntity.DayEntity
import com.antuere.musicapp.data.localDatabase.util.EntityMapper
import com.antuere.musicapp.networkApi.musicProfileDto.DayDto

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