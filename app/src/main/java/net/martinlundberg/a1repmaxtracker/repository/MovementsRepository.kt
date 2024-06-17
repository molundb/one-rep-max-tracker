package net.martinlundberg.a1repmaxtracker.repository

import net.martinlundberg.a1repmaxtracker.feature.movementdetail.MovementDetail
import net.martinlundberg.a1repmaxtracker.feature.movementslist.Movement

interface MovementsRepository {

    fun getMovements(): List<Movement>

    fun getMovementDetail(id: Int): MovementDetail

    fun addMovement(movement: Movement): Boolean

    fun updateMovement(movement: Movement): Boolean

    fun deleteMovement(id: Int): Boolean
}