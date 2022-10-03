package com.antuere.musicapp.di

import android.app.Application
import androidx.room.Room
import com.antuere.data.localDatabase.MusicProfileDataBase
import com.antuere.data.networkApi.MusicApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory())
            .build()
    }


    @Provides
    @Singleton
    fun provideApi(moshi: Moshi): MusicApiService {
        val baseUrl = "http://192.168.0.22:5432/"

        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(baseUrl)
            .build()
            .create(MusicApiService::class.java)
    }


    @Provides
    @Singleton
    fun provideMusicProfileDataBase(application: Application): MusicProfileDataBase {
        return Room.databaseBuilder(
            application.applicationContext,
            MusicProfileDataBase::class.java,
            "music_profiles"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNameProfile(): String = "Test Profile"

}