package com.example.musicapp.network.mappers

import com.example.musicapp.database.musicProfileEntity.MusicProfileEntity
import com.example.musicapp.database.util.EntityMapper
import com.example.musicapp.network.musicProfileDto.MusicProfileDto

class MusicProfileDtoMapper(private val scheduleDtoMapper: ScheduleDtoMapper) :
    EntityMapper<MusicProfileDto, MusicProfileEntity> {

    override fun mapToEntity(t: MusicProfileDto): MusicProfileEntity {
        return MusicProfileEntity(
            id = t.id,
            name = t.name,
            schedule = scheduleDtoMapper.mapToEntity(t.schedule)
        )
    }

    override fun mapFromEntity(entity: MusicProfileEntity): MusicProfileDto {
        return MusicProfileDto(
            id = entity.id,
            name = entity.name,
            schedule = scheduleDtoMapper.mapFromEntity(entity.schedule)
        )
    }

}