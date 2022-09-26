package com.example.musicapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.musicapp.database.MusicProfileDataBase
import com.example.musicapp.domain.MusicProfile
import com.example.musicapp.network.MusicApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class MusicProfileRepository(
    private val database: MusicProfileDataBase,
    nameProfile: String
) {

    val musicProfile: LiveData<MusicProfile> =
        Transformations.map(database.musicProfileDAO.getProfile(nameProfile)) {
            it?.asDomain()
        }

    suspend fun updateProfile() {
        Timber.i("my log we in startUpdateProfile")
        withContext(Dispatchers.IO) {
            Timber.i("my log we in coroutine startUpdateProfile")
            val profile = MusicApi.retrofitService.getJSON().asMusicProfileDB()

            Timber.i("my log we in coroutine after getJson")

            database.musicProfileDAO.insertProfile(profile)
            Timber.i("my log we in coroutine after insertProfile")
        }
    }
}