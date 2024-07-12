package net.martinlundberg.a1repmaxtracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import net.martinlundberg.a1repmaxtracker.data.database.model.MovementEntity
import net.martinlundberg.a1repmaxtracker.data.database.model.ResultEntity

@Dao
interface MovementDao {
    @Query("SELECT * FROM movementEntity LEFT JOIN resultEntity ON movementEntity.id = resultEntity.movementId")
    fun getMovements(): Flow<Map<MovementEntity, List<ResultEntity>>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movement: MovementEntity): Long

    @Query("DELETE FROM movementEntity WHERE id = :id")
    suspend fun deleteById(id: Long)
}