package net.martinlundberg.a1repmaxtracker.repository

import net.martinlundberg.a1repmaxtracker.database.dao.MovementDao
import net.martinlundberg.a1repmaxtracker.database.model.asExternalMovement
import net.martinlundberg.a1repmaxtracker.database.model.asExternalMovementDetail
import net.martinlundberg.a1repmaxtracker.feature.movementdetail.MovementDetail
import net.martinlundberg.a1repmaxtracker.feature.movementslist.Movement

class DefaultMovementsRepository(private val movementDao: MovementDao) : MovementsRepository {
    override fun getMovements(): List<Movement> =
        movementDao.getMovements()
            .map { it.asExternalMovement() }

    override fun getMovementDetail(id: Int): MovementDetail =
        movementDao.getMovement(id).asExternalMovementDetail()

    override fun addMovement(movement: Movement) = movementDao.insert(movement)

    override fun updateMovement(movement: Movement): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteMovement(id: Int): Boolean {
        TODO("Not yet implemented")
    }
}