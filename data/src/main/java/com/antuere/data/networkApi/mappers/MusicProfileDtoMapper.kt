package com.antuere.data.networkApi.mappers

import com.antuere.data.localDatabase.musicProfileEntity.MusicProfileEntity
import com.antuere.data.localDatabase.util.EntityMapper
import com.antuere.data.networkApi.musicProfileDto.MusicProfileDto

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