package com.antuere.data.localDatabase.util

import androidx.room.TypeConverter
import com.antuere.data.localDatabase.musicProfileEntity.DayEntity
import com.antuere.data.localDatabase.musicProfileEntity.PlaylistEntity
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun listDaysToJson(value: List<DayEntity>): String = Gson().toJson(value)

    @TypeConverter
    fun daysJsonToListDays(value: String) = Gson().fromJson(value, Array<DayEntity>::class.java).toList()

    @TypeConverter
    fun listPlaylistsToJson(value: List<PlaylistEntity>): String = Gson().toJson(value)

    @TypeConverter
    fun playlistsJsonToListPlaylist(value: String) =
        Gson().fromJson(value, Array<PlaylistEntity>::class.java).toList()

}