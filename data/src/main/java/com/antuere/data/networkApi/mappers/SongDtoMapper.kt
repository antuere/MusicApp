package com.antuere.data.networkApi.mappers

import com.antuere.data.localDatabase.musicProfileEntity.SongEntity
import com.antuere.data.localDatabase.util.EntityMapper
import com.antuere.data.networkApi.musicProfileDto.SongDto

class SongDtoMapper :
    EntityMapper<SongDto, SongEntity> {


    override fun mapToEntity(t: SongDto): SongEntity {
        return SongEntity(
            t.duration,
            t.url,
            t.fileId,
            t.md5File,
            t.name,
            t.order,
            t.size
        )
    }

    override fun mapFromEntity(entity: SongEntity): SongDto {
        return SongDto(
            entity.duration,
            entity.url,
            entity.fileId,
            entity.md5File,
            entity.name,
            entity.order,
            entity.size
        )
    }
}