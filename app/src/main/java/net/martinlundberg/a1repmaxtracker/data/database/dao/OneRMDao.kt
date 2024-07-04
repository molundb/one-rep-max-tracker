package net.martinlundberg.a1repmaxtracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import net.martinlundberg.a1repmaxtracker.data.database.model.OneRMEntity

@Dao
interface OneRMDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(oneRM: OneRMEntity)

    @Query("SELECT * FROM oneRMEntity WHERE oneRMid = :id")
    fun getOneRM(id: Long): Flow<OneRMEntity>

    @Query("SELECT * FROM oneRMEntity WHERE movementId = :id")
    fun getOneRMsForMovement(id: Long): Flow<List<OneRMEntity>>

    @Query("DELETE FROM oneRMEntity WHERE oneRMid = :id")
    suspend fun deleteByOneRMId(id: Long)

    @Query("DELETE FROM oneRMEntity WHERE movementId = :id")
    suspend fun deleteAllWithMovementId(id: Long)
}