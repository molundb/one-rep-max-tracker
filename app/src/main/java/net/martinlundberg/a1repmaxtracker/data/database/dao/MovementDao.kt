package net.martinlundberg.a1repmaxtracker.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.martinlundberg.a1repmaxtracker.data.database.model.MovementEntity
import net.martinlundberg.a1repmaxtracker.data.database.model.OneRMEntity

@Dao
interface MovementDao {
    @Query("SELECT * FROM movementEntity")
    fun getMovements(): List<MovementEntity>

//    @Query("SELECT * FROM movementEntity WHERE id = :id")
//    fun getMovement(id: Int): MovementEntity

    @Query(
        "SELECT * FROM movementEntity JOIN oneRMEntity ON movementEntity.id = oneRMEntity.movementId WHERE id = :id "
    )
    fun getMovement(id: Int): Map<MovementEntity, List<OneRMEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movement: MovementEntity)

    @Delete
    fun delete(movement: MovementEntity)
}