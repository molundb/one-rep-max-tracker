package net.martinlundberg.a1repmaxtracker.repository

import net.martinlundberg.a1repmaxtracker.feature.movementdetail.MovementDetail
import net.martinlundberg.a1repmaxtracker.feature.movementslist.Movement

class DefaultMovementsRepository : MovementsRepository {
    override fun getMovements(): List<Movement> {
        TODO("Not yet implemented")
    }

    override fun getMovementDetail(id: Int): MovementDetail {
        TODO("Not yet implemented")
    }

    override fun addMovement(movement: Movement): Boolean {
        TODO("Not yet implemented")
    }

    override fun updateMovement(movement: Movement): Boolean {
        TODO("Not yet implemented")
    }

    override fun deleteMovement(id: Int): Boolean {
        TODO("Not yet implemented")
    }
}