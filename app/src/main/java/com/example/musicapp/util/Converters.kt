package com.example.musicapp.util

import androidx.room.TypeConverter
import com.example.musicapp.database.musicProfileDB.DayDB
import com.example.musicapp.database.musicProfileDB.PlaylistDB
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun listDaysToJson(value: List<DayDB>): String = Gson().toJson(value)

    @TypeConverter
    fun daysJsonToListDays(value: String) = Gson().fromJson(value, Array<DayDB>::class.java).toList()

    @TypeConverter
    fun listPlaylistsToJson(value: List<PlaylistDB>): String = Gson().toJson(value)

    @TypeConverter
    fun playlistsJsonToListPlaylist(value: String) =
        Gson().fromJson(value, Array<PlaylistDB>::class.java).toList()
}