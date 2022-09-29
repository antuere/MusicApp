package com.antuere.domain.repository

import com.antuere.domain.musicProfile.MusicProfile
import kotlinx.coroutines.flow.Flow

interface MusicProfileRepository {

    fun getProfile(): Flow<MusicProfile?>

    suspend fun updateProfile()
}