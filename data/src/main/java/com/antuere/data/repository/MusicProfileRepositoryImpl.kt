package com.antuere.data.repository

import com.antuere.data.localDatabase.MusicProfileDataBase
import com.antuere.data.localDatabase.mappers.*
import com.antuere.data.networkApi.MusicApiService
import com.antuere.data.networkApi.mappers.*
import com.antuere.domain.musicProfile.MusicProfile
import com.antuere.domain.repository.MusicProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MusicProfileRepositoryImpl @Inject constructor(
    private val database: MusicProfileDataBase,
    private val musicProfileApi: MusicApiService,
    private val nameProfile: String,
    private val musicProfileEntityMapper: MusicProfileEntityMapper,
    private val musicProfileDtoMapper: MusicProfileDtoMapper
) : MusicProfileRepository {


    override fun getProfile(): Flow<MusicProfile?> {
        val musicProfile: Flow<MusicProfile?> =
            database.musicProfileDAO.getProfile(nameProfile).map {
                it?.let { profile ->
                    musicProfileEntityMapper.mapToDomainModel(profile)
                }
            }
        return musicProfile
    }

    override suspend fun updateProfile() {
        withContext(Dispatchers.IO) {
            val profileJson = musicProfileApi.getJSON()
            val profile = musicProfileDtoMapper.mapToEntity(profileJson)

            database.musicProfileDAO.insertProfile(profile)
        }
    }
}