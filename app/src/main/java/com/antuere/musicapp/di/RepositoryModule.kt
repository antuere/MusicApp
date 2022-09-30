package com.antuere.musicapp.di

import com.antuere.data.repository.MusicProfileRepositoryImpl
import com.antuere.domain.repository.MusicProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMusicRepository(
        musicProfileRepositoryImpl: MusicProfileRepositoryImpl
    ): MusicProfileRepository
}