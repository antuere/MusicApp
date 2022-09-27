package com.example.musicapp.network.mappers

import com.example.musicapp.database.musicProfileEntity.SongEntity
import com.example.musicapp.database.util.EntityMapper
import com.example.musicapp.network.musicProfileDto.SongDto

class SongDtoMapper : EntityMapper<SongDto, SongEntity> {


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