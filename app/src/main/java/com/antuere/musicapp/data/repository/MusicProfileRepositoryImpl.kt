package com.antuere.musicapp.data.repository

import com.antuere.musicapp.data.localDatabase.MusicProfileDataBase
import com.antuere.musicapp.data.localDatabase.mappers.*
import com.antuere.musicapp.domain.musicProfile.MusicProfile
import com.antuere.musicapp.domain.repository.MusicProfileRepository
import com.antuere.musicapp.networkApi.MusicApi
import com.antuere.musicapp.networkApi.mappers.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MusicProfileRepositoryImpl(
    private val database: MusicProfileDataBase,
    private val nameProfile: String
) : MusicProfileRepository {


    override fun getProfile(): Flow<MusicProfile?> {
        val musicProfile: Flow<MusicProfile?> =
            database.musicProfileDAO.getProfile(nameProfile).map {
                val playlistZoneEntityMapper = PlaylistZoneEntityMapper()
                val songEntityMapper = SongEntityMapper()
                val playlistEntityMapper = PlaylistEntityMapper(songEntityMapper)
                val timeZoneEntityMapper = TimeZoneEntityMapper(playlistZoneEntityMapper)
                val dayEntityMapper = DayEntityMapper(timeZoneEntityMapper)
                val scheduleEntityMapper =
                    ScheduleEntityMapper(dayEntityMapper, playlistEntityMapper)
                val musicProfileEntityMapper = MusicProfileEntityMapper(scheduleEntityMapper)
                it?.let { profile ->
                    musicProfileEntityMapper.mapToDomainModel(profile)
                }
            }
        return musicProfile
    }

    override suspend fun updateProfile() {
        withContext(Dispatchers.IO) {
            val playlistZoneDtoMapper = PlaylistZoneDtoMapper()
            val songDtoMapper = SongDtoMapper()
            val playlistDtoMapper = PlaylistDtoMapper(songDtoMapper)
            val timeZoneDtoMapper = TimeZoneDtoMapper(playlistZoneDtoMapper)
            val dayDtoMapper = DayDtoMapper(timeZoneDtoMapper)
            val scheduleDtoMapper = ScheduleDtoMapper(dayDtoMapper, playlistDtoMapper)
            val musicProfileDtoMapper = MusicProfileDtoMapper(scheduleDtoMapper)

            val profileJson = MusicApi.retrofitService.getJSON()
            val profile = musicProfileDtoMapper.mapToEntity(profileJson)

            database.musicProfileDAO.insertProfile(profile)
        }
    }
}