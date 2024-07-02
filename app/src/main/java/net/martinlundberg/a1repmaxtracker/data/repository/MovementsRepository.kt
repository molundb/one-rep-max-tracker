package net.martinlundberg.a1repmaxtracker.data.repository

import net.martinlundberg.a1repmaxtracker.data.model.Movement
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo

interface MovementsRepository {

    suspend fun getMovements(): List<Movement>

    suspend fun getMovementDetail(id: Long): MovementDetail

    suspend fun setMovement(movement: Movement): Long

    suspend fun deleteMovement(id: Long)

    suspend fun addOneRM(oneRM: OneRMInfo, movementId: Long)

    suspend fun getOneRM(id: Long): OneRMInfo
}