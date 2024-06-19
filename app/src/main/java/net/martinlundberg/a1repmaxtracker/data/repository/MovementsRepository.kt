package net.martinlundberg.a1repmaxtracker.data.repository

import net.martinlundberg.a1repmaxtracker.data.model.Movement
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo

interface MovementsRepository {

    suspend fun getMovements(): List<Movement>

    suspend fun getMovementDetail(id: Int): MovementDetail

    suspend fun addMovement(movement: Movement)

    fun updateMovement(movement: Movement): Boolean

    suspend fun deleteMovement(id: Int)

    suspend fun addOneRM(oneRM: OneRMInfo, movementId: Int)
}