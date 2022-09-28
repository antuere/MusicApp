package com.example.musicapp.domain.usecase

import com.example.musicapp.repository.MusicProfileRepository

class UpdateMusicProfileUseCase(private val musicProfileRepository: MusicProfileRepository) {

    suspend operator fun invoke() {
        return musicProfileRepository.updateProfile()
    }
}