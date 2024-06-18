package net.martinlundberg.a1repmaxtracker.data.repository

import net.martinlundberg.a1repmaxtracker.data.database.dao.MovementDao
import net.martinlundberg.a1repmaxtracker.data.database.model.asExternalMovement
import net.martinlundberg.a1repmaxtracker.data.database.model.asExternalMovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.Movement
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.asEntity

class DefaultMovementsRepository(private val movementDao: MovementDao) : MovementsRepository {
    override suspend fun getMovements(): List<Movement> =
        movementDao.getMovements()
            .map { it.asExternalMovement() }

    override suspend fun getMovementDetail(id: Int): MovementDetail =
        movementDao.getMovement(id).asExternalMovementDetail()

    override suspend fun addMovement(movement: Movement) = movementDao.insert(movement.asEntity())

    override fun updateMovement(movement: Movement): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteMovement(id: Int): Boolean {
        TODO("Not yet implemented")
    }
}