package net.martinlundberg.a1repmaxtracker.data.repository

import net.martinlundberg.a1repmaxtracker.data.model.Movement
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo

interface MovementsRepository {

    suspend fun getMovements(): List<Movement>

    suspend fun getMovementDetail(id: Long): MovementDetail

    suspend fun addMovement(movement: Movement): Long

    fun updateMovement(movement: Movement): Boolean

    suspend fun deleteMovement(id: Int)

    suspend fun addOneRM(oneRM: OneRMInfo, movementId: Long)
}