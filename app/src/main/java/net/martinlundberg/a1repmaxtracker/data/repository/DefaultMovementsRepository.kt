package net.martinlundberg.a1repmaxtracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.martinlundberg.a1repmaxtracker.data.database.dao.MovementDao
import net.martinlundberg.a1repmaxtracker.data.database.dao.OneRMDao
import net.martinlundberg.a1repmaxtracker.data.database.model.asExternalMovement
import net.martinlundberg.a1repmaxtracker.data.model.Movement
import net.martinlundberg.a1repmaxtracker.data.model.asEntity
import javax.inject.Inject

class DefaultMovementsRepository @Inject constructor(
    private val movementDao: MovementDao,
    private val oneRMDao: OneRMDao,
) : MovementsRepository {
    override fun getMovements(): Flow<List<Movement>> =
        movementDao.getMovements().map { map ->
            map.entries.map {
                it.asExternalMovement()
            }
        }

    override suspend fun setMovement(movement: Movement) = movementDao.insert(movement.asEntity())

    override suspend fun deleteMovement(movementId: Long) {
        movementDao.deleteById(movementId)
        oneRMDao.deleteAllWithMovementId(movementId)
    }
}