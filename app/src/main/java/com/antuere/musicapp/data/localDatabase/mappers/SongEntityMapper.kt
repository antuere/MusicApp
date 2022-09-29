package com.antuere.musicapp.data.localDatabase.mappers

import com.antuere.musicapp.data.localDatabase.musicProfileEntity.SongEntity
import com.antuere.musicapp.domain.musicProfile.Song
import com.antuere.musicapp.domain.util.DomainMapper

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