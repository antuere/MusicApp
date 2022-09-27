package com.example.musicapp.database.mappers

import com.example.musicapp.database.musicProfileEntity.PlaylistEntity
import com.example.musicapp.domain.Playlist
import com.example.musicapp.domain.util.DomainMapper

class PlaylistEntityMapper(private val songEntityMapper: SongEntityMapper) :
    DomainMapper<PlaylistEntity, Playlist> {

    override fun mapToDomainModel(model: PlaylistEntity): Playlist {
        return Playlist(
            model.duration,
            songs = model.songs.map { songEntityMapper.mapToDomainModel(it) },
            model.id,
            model.name,
            model.random
        )
    }

    override fun mapFromDomainModel(domainModel: Playlist): PlaylistEntity {
        return PlaylistEntity(
            domainModel.duration,
            songs = domainModel.songs.map { songEntityMapper.mapFromDomainModel(it) },
            domainModel.id,
            domainModel.name,
            domainModel.random
        )
    }
}