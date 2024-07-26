package net.martinlundberg.onerepmaxtracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsHelper
import net.martinlundberg.onerepmaxtracker.analytics.logDeleteMovement
import net.martinlundberg.onerepmaxtracker.data.database.dao.MovementDao
import net.martinlundberg.onerepmaxtracker.data.database.dao.ResultDao
import net.martinlundberg.onerepmaxtracker.data.database.model.asExternalMovement
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import javax.inject.Inject

class DefaultMovementsRepository @Inject constructor(
    private val movementDao: MovementDao,
    private val resultDao: ResultDao,
    private val analyticsHelper: AnalyticsHelper,
) : MovementsRepository {
    override suspend fun getMovements(): Flow<List<Movement>> =
        movementDao.getMovements().map { map ->
            map.entries.map {
                it.asExternalMovement()
            }
        }

    override suspend fun setMovement(movement: Movement) = movementDao.insert(movement.asEntity())

    override suspend fun deleteMovement(movementId: Long) {
        analyticsHelper.logDeleteMovement(movementId)
        movementDao.deleteById(movementId)
        resultDao.deleteAllWithMovementId(movementId)
    }
}