package net.martinlundberg.onerepmaxtracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import net.martinlundberg.onerepmaxtracker.data.database.model.MovementEntity
import net.martinlundberg.onerepmaxtracker.data.database.model.ResultEntity

@Dao
interface ResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: ResultEntity)

    @Query("SELECT * FROM resultEntity WHERE resultId = :id")
    fun getResult(id: Long): Flow<ResultEntity?>

    @Query(
        value = """
        SELECT * FROM movementEntity
        LEFT JOIN resultEntity ON movementEntity.id = resultEntity.movementId 
        WHERE movementEntity.id = :movementId
        ORDER BY resultEntity.date DESC
        """,
    )
    fun getResultsForMovement(movementId: Long): Flow<Map<MovementEntity, List<ResultEntity>>>

    @Query("DELETE FROM resultEntity WHERE resultId = :id")
    suspend fun deleteByResultId(id: Long)

    @Query("DELETE FROM resultEntity WHERE movementId = :id")
    suspend fun deleteAllWithMovementId(id: Long)
}