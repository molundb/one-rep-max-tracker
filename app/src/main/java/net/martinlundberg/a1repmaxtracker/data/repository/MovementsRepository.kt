package net.martinlundberg.a1repmaxtracker.data.repository

import net.martinlundberg.a1repmaxtracker.data.model.Movement
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail

interface MovementsRepository {

    fun getMovements(): List<Movement>

    fun getMovementDetail(id: Int): MovementDetail

    fun addMovement(movement: Movement)

    fun updateMovement(movement: Movement): Boolean

    fun deleteMovement(id: Int): Boolean
}