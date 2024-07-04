package net.martinlundberg.a1repmaxtracker.data.repository

import net.martinlundberg.a1repmaxtracker.data.database.dao.MovementDao
import net.martinlundberg.a1repmaxtracker.data.database.model.asExternalMovement
import net.martinlundberg.a1repmaxtracker.data.model.Movement
import net.martinlundberg.a1repmaxtracker.data.model.asEntity
import javax.inject.Inject

class DefaultMovementsRepository @Inject constructor(
    private val movementDao: MovementDao,
) : MovementsRepository {
    override suspend fun getMovements(): List<Movement> =
        movementDao.getMovements().entries.map {
            it.asExternalMovement()
        }

    override suspend fun setMovement(movement: Movement) = movementDao.insert(movement.asEntity())

    override suspend fun deleteMovement(id: Long) = movementDao.deleteById(id)
}