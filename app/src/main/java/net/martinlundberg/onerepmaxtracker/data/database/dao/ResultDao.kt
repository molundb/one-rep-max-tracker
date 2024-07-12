package net.martinlundberg.onerepmaxtracker.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import net.martinlundberg.onerepmaxtracker.data.database.model.ResultEntity

@Dao
interface ResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: ResultEntity)

    @Query("SELECT * FROM resultEntity WHERE resultId = :id")
    fun getResult(id: Long): Flow<ResultEntity?>

    @Query("SELECT * FROM resultEntity WHERE movementId = :id")
    fun getResultsForMovement(id: Long): Flow<List<ResultEntity>>

    @Query("DELETE FROM resultEntity WHERE resultId = :id")
    suspend fun deleteByResultId(id: Long)

    @Query("DELETE FROM resultEntity WHERE movementId = :id")
    suspend fun deleteAllWithMovementId(id: Long)
}