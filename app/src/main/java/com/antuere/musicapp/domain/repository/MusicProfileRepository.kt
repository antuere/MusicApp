package com.antuere.musicapp.domain.repository

import com.antuere.musicapp.domain.musicProfile.MusicProfile
import kotlinx.coroutines.flow.Flow

interface MusicProfileRepository {

    fun getProfile(): Flow<MusicProfile?>

    suspend fun updateProfile()
}