package com.antuere.musicapp.di

import com.antuere.domain.repository.MusicProfileRepository
import com.antuere.domain.usecase.GetMusicProfileUseCase
import com.antuere.domain.usecase.UpdateMusicProfileUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    @Provides
    fun provideGetMusicProfileUseCase(repository: MusicProfileRepository): GetMusicProfileUseCase {
        return GetMusicProfileUseCase(repository)
    }

    @Provides
    fun provideUpdateMusicProfileUseCase(repository: MusicProfileRepository): UpdateMusicProfileUseCase {
        return UpdateMusicProfileUseCase(repository)
    }
}