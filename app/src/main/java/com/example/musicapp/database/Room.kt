package com.example.musicapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.musicapp.util.Converters

@Dao
interface MusicProfileDAO {

    @Query("SELECT * FROM MusicProfileDB WHERE name = :profileName")
    fun getProfile(profileName: String): LiveData<MusicProfileDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProfile(profile: MusicProfileDB)

    @Update
    fun updateProfile(profile: MusicProfileDB)
}


@Database(entities = [MusicProfileDB::class], version = 1)
@TypeConverters(Converters::class)
abstract class MusicProfileDataBase() : RoomDatabase() {
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