package net.martinlundberg.a1repmaxtracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.martinlundberg.a1repmaxtracker.data.database.model.OneRMEntity

@Dao
interface OneRMDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(oneRM: OneRMEntity)

    @Query("SELECT * FROM oneRMEntity WHERE oneRMid = :id")
    suspend fun getOneRM(id: Long): OneRMEntity

    @Query("SELECT * FROM oneRMEntity WHERE movementId = :id")
    suspend fun getOneRMsForMovement(id: Long): List<OneRMEntity>
}