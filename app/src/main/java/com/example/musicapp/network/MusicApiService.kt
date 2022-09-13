package com.example.musicapp.network

import com.example.musicapp.network.musicProfile.MusicProfile
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


private const val BASE_URL = "http://192.168.1.53:5000/"

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


interface MusicApiService {

    @GET("returnjson")
    suspend fun getJSON(): MusicProfile

}

object MusicApi {
    val retrofitService: MusicApiService by lazy {
        retrofit.create(MusicApiService::class.java)
    }
}