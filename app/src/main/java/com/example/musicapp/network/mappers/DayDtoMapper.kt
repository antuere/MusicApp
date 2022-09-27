package com.example.musicapp.network.mappers

import com.example.musicapp.database.musicProfileEntity.DayEntity
import com.example.musicapp.database.util.EntityMapper
import com.example.musicapp.network.musicProfileDto.DayDto

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