package com.antuere.musicapp.domain.usecase

import com.antuere.musicapp.domain.repository.MusicProfileRepository

class UpdateMusicProfileUseCase(private val musicProfileRepository: MusicProfileRepository) {

    suspend operator fun invoke() {
        return musicProfileRepository.updateProfile()
    }
}