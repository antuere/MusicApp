package com.antuere.data.localDatabase

import androidx.room.*
import com.antuere.data.localDatabase.musicProfileEntity.MusicProfileEntity
import com.antuere.data.localDatabase.util.Converters
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicProfileDAO {

    @Query("SELECT * FROM MusicProfileEntity WHERE name = :profileName")
    fun getProfile(profileName: String): Flow<MusicProfileEntity?>

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
