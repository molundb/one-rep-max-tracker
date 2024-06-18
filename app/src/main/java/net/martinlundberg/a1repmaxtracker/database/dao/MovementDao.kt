package net.martinlundberg.a1repmaxtracker.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.martinlundberg.a1repmaxtracker.database.model.MovementEntity

@Dao
interface MovementDao {
    @Query("SELECT * FROM movementEntity")
    fun getAll(): List<MovementEntity>

    @Query("SELECT * FROM movementEntity WHERE id = :id")
    fun get(id: Int): List<MovementEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movement: MovementEntity)

    @Delete
    fun delete(movement: MovementEntity)
}