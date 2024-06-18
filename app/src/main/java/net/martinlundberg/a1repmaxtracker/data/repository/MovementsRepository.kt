package net.martinlundberg.a1repmaxtracker.data.repository

import net.martinlundberg.a1repmaxtracker.data.model.Movement
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail

interface MovementsRepository {

    suspend fun getMovements(): List<Movement>

    suspend fun getMovementDetail(id: Int): MovementDetail

    suspend fun addMovement(movement: Movement)

    fun updateMovement(movement: Movement): Boolean

    suspend fun deleteMovement(id: Int)
}