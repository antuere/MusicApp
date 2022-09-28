package com.example.musicapp.domain.usecase

import androidx.lifecycle.LiveData
import com.example.musicapp.domain.MusicProfile
import com.example.musicapp.repository.MusicProfileRepository

class GetMusicProfileUseCase(private val musicProfileRepository: MusicProfileRepository) {

    operator fun invoke(): LiveData<MusicProfile> {
        return musicProfileRepository.getProfile()
    }
}