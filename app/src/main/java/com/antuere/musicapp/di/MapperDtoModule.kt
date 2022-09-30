package com.antuere.musicapp.di

import com.antuere.data.networkApi.mappers.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class MapperDtoModule {

    @Provides
    @Singleton
    fun provideMusicProfileDtoMapper(scheduleDtoMapper: ScheduleDtoMapper): MusicProfileDtoMapper {
        return MusicProfileDtoMapper(scheduleDtoMapper)
    }

    @Provides
    @Singleton
    fun provideScheduleDtoMapper(
        dayDtoMapper: DayDtoMapper,
        playlistDtoMapper: PlaylistDtoMapper
    ): ScheduleDtoMapper {
        return ScheduleDtoMapper(dayDtoMapper, playlistDtoMapper)
    }

    @Provides
    @Singleton
    fun provideDayDtoMapper(timeZoneDtoMapper: TimeZoneDtoMapper): DayDtoMapper {
        return DayDtoMapper(timeZoneDtoMapper)
    }

    @Provides
    @Singleton
    fun provideTimeZoneDtoMapper(playlistZoneDtoMapper: PlaylistZoneDtoMapper): TimeZoneDtoMapper {
        return TimeZoneDtoMapper(playlistZoneDtoMapper)
    }

    @Provides
    @Singleton
    fun providePlaylistDtoMapper(songDtoMapper: SongDtoMapper): PlaylistDtoMapper {
        return PlaylistDtoMapper(songDtoMapper)
    }

    @Provides
    @Singleton
    fun providePlaylistZoneDtoMapper(): PlaylistZoneDtoMapper {
        return PlaylistZoneDtoMapper()
    }

    @Provides
    @Singleton
    fun provideSongDtoMapper(): SongDtoMapper {
        return SongDtoMapper()
    }
}