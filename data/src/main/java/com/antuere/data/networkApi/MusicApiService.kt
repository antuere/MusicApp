package com.antuere.data.networkApi

import com.antuere.data.networkApi.musicProfileDto.MusicProfileDto
import retrofit2.http.GET

interface MusicApiService {

    @GET("schedule")
    suspend fun getJSON(): MusicProfileDto

}
