package com.antuere.domain.usecase

import com.antuere.domain.musicProfile.MusicProfile
import com.antuere.domain.repository.MusicProfileRepository
import kotlinx.coroutines.flow.Flow

class GetMusicProfileUseCase(private val musicProfileRepository: MusicProfileRepository) {

    operator fun invoke(): Flow<MusicProfile?> {

        return musicProfileRepository.getProfile()
    }
}