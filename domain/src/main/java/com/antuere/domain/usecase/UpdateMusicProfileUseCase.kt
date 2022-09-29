package com.antuere.domain.usecase

import com.antuere.domain.repository.MusicProfileRepository

class UpdateMusicProfileUseCase(private val musicProfileRepository: MusicProfileRepository) {

    suspend operator fun invoke() {
        return musicProfileRepository.updateProfile()
    }
}