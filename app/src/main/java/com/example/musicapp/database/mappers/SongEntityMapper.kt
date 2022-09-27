package com.example.musicapp.database.mappers

import com.example.musicapp.database.musicProfileEntity.SongEntity
import com.example.musicapp.domain.Song
import com.example.musicapp.domain.util.DomainMapper

class SongEntityMapper : DomainMapper<SongEntity, Song> {

    override fun mapToDomainModel(model: SongEntity): Song {
        return Song(
            model.duration,
            model.url,
            model.fileId,
            model.md5File,
            model.name,
            model.order,
            model.size
        )
    }

    override fun mapFromDomainModel(domainModel: Song): SongEntity {
        return SongEntity(
            domainModel.duration,
            domainModel.url,
            domainModel.fileId,
            domainModel.md5File,
            domainModel.name,
            domainModel.order,
            domainModel.size
        )
    }
}