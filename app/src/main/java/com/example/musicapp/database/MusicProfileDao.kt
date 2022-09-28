package com.example.musicapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.musicapp.database.musicProfileEntity.MusicProfileEntity
import com.example.musicapp.database.util.Converters

@Dao
interface MusicProfileDAO {

    @Query("SELECT * FROM MusicProfileEntity WHERE name = :profileName")
    fun getProfile(profileName: String): LiveData<MusicProfileEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProfile(profile: MusicProfileEntity)

    @Update
    fun updateProfile(profile: MusicProfileEntity)
}


@Database(entities = [MusicProfileEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class MusicProfileDataBase : RoomDatabase() {
    abstract val musicProfileDAO: MusicProfileDAO
}

private lateinit var INSTANCE: MusicProfileDataBase

fun getDataBase(context: Context): MusicProfileDataBase {
    synchronized(MusicProfileDataBase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                MusicProfileDataBase::class.java,
                "music_profiles"
            ).build()
        }
        return INSTANCE
    }
}