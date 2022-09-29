package com.antuere.musicapp.domain.usecase

import com.antuere.musicapp.domain.musicProfile.MusicProfile
import com.antuere.musicapp.domain.repository.MusicProfileRepository
import kotlinx.coroutines.flow.Flow

class GetMusicProfileUseCase(private val musicProfileRepository: MusicProfileRepository) {

    operator fun invoke(): Flow<MusicProfile?> {

        return musicProfileRepository.getProfile()
    }
}