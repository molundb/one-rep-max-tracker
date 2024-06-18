package net.martinlundberg.a1repmaxtracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.martinlundberg.a1repmaxtracker.data.database.model.MovementEntity
import net.martinlundberg.a1repmaxtracker.data.database.model.OneRMEntity

@Dao
interface MovementDao {
    @Query("SELECT * FROM movementEntity")
    suspend fun getMovements(): List<MovementEntity>

//    @Query("SELECT * FROM movementEntity WHERE id = :id")
//    fun getMovement(id: Int): MovementEntity

    @Query(
        "SELECT * FROM movementEntity JOIN oneRMEntity ON movementEntity.id = oneRMEntity.movementId WHERE id = :id "
    )
    suspend fun getMovement(id: Int): Map<MovementEntity, List<OneRMEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movement: MovementEntity)

    @Query("DELETE FROM movementEntity WHERE id = :id")
    suspend fun deleteById(id: Int)
}