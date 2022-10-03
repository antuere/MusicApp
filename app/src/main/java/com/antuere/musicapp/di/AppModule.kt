package com.antuere.musicapp.di

import android.app.Application
import com.antuere.domain.usecase.GetMusicProfileUseCase
import com.antuere.musicapp.util.MyMusicDownloader
import com.antuere.musicapp.util.MyPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMyMusicPlayer(application: Application): MyPlayer {
        return MyPlayer(application)
    }

    @Provides
    @Singleton
    fun provideMyMusicDownloader(
        application: Application
    ): MyMusicDownloader {
        return MyMusicDownloader(application.applicationContext)
    }
}