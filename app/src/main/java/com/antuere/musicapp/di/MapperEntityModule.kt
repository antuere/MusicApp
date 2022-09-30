package com.antuere.musicapp.di

import com.antuere.data.localDatabase.mappers.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class MapperEntityModule {

    @Provides
    @Singleton
    fun provideMusicProfileEntityMapper(scheduleEntityMapper: ScheduleEntityMapper): MusicProfileEntityMapper {
        return MusicProfileEntityMapper(scheduleEntityMapper)
    }

    @Provides
    @Singleton
    fun provideScheduleEntityMapper(
        dayEntityMapper: DayEntityMapper,
        playlistEntityMapper: PlaylistEntityMapper
    ): ScheduleEntityMapper {
        return ScheduleEntityMapper(dayEntityMapper, playlistEntityMapper)
    }

    @Provides
    @Singleton
    fun provideDayEntityMapper(timeZoneEntityMapper: TimeZoneEntityMapper): DayEntityMapper {
        return DayEntityMapper(timeZoneEntityMapper)
    }

    @Provides
    @Singleton
    fun provideTimeZoneEntityMapper(playlistZoneEntityMapper: PlaylistZoneEntityMapper): TimeZoneEntityMapper {
        return TimeZoneEntityMapper(playlistZoneEntityMapper)
    }

    @Provides
    @Singleton
    fun providePlaylistEntityMapper(songEntityMapper: SongEntityMapper): PlaylistEntityMapper {
        return PlaylistEntityMapper(songEntityMapper)
    }

    @Provides
    @Singleton
    fun providePlaylistZoneEntityMapper(): PlaylistZoneEntityMapper {
        return PlaylistZoneEntityMapper()
    }

    @Provides
    @Singleton
    fun provideSongEntityMapper(): SongEntityMapper {
        return SongEntityMapper()
    }
}