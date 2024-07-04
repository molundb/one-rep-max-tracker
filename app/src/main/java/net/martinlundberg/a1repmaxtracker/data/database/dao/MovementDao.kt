package net.martinlundberg.a1repmaxtracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.martinlundberg.a1repmaxtracker.data.database.model.MovementEntity
import net.martinlundberg.a1repmaxtracker.data.database.model.OneRMEntity

@Dao
interface MovementDao {
    @Query("SELECT * FROM movementEntity LEFT JOIN oneRMEntity ON movementEntity.id = oneRMEntity.movementId")
    suspend fun getMovements(): Map<MovementEntity, List<OneRMEntity>>

    @Query("SELECT * FROM oneRMEntity WHERE movementId = :id")
    suspend fun getMovement(id: Long): List<OneRMEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movement: MovementEntity): Long

    @Query("DELETE FROM movementEntity WHERE id = :id")
    suspend fun deleteById(id: Long)
}