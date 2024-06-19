package net.martinlundberg.a1repmaxtracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import net.martinlundberg.a1repmaxtracker.data.database.model.OneRMEntity

@Dao
interface OneRMDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(oneRM: OneRMEntity)
}