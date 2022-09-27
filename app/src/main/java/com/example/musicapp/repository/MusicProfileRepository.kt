package com.example.musicapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.musicapp.database.MusicProfileDataBase
import com.example.musicapp.database.mappers.*
import com.example.musicapp.domain.MusicProfile
import com.example.musicapp.network.MusicApi
import com.example.musicapp.network.mappers.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MusicProfileRepository(
    private val database: MusicProfileDataBase,
    nameProfile: String
) {

    val musicProfile: LiveData<MusicProfile> =
        Transformations.map(database.musicProfileDAO.getProfile(nameProfile)) {
            val playlistZoneEntityMapper = PlaylistZoneEntityMapper()
            val songEntityMapper = SongEntityMapper()
            val playlistEntityMapper = PlaylistEntityMapper(songEntityMapper)
            val timeZoneEntityMapper = TimeZoneEntityMapper(playlistZoneEntityMapper)
            val dayEntityMapper = DayEntityMapper(timeZoneEntityMapper)
            val scheduleEntityMapper = ScheduleEntityMapper(dayEntityMapper, playlistEntityMapper)
            val musicProfileEntityMapper = MusicProfileEntityMapper(scheduleEntityMapper)
            it?.let {profile ->
                musicProfileEntityMapper.mapToDomainModel(profile)
            }
        }

    suspend fun updateProfile() {
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