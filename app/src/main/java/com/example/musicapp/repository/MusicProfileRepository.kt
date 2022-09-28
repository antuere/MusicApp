package com.example.musicapp.repository

import androidx.lifecycle.LiveData
import com.example.musicapp.domain.MusicProfile

interface MusicProfileRepository {

    fun getProfile(): LiveData<MusicProfile>

    suspend fun updateProfile()
}