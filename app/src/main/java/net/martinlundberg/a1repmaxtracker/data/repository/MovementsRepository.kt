package net.martinlundberg.a1repmaxtracker.data.repository

import kotlinx.coroutines.flow.Flow
import net.martinlundberg.a1repmaxtracker.data.model.Movement

interface MovementsRepository {

    suspend fun getMovements(): Flow<List<Movement>>

    suspend fun setMovement(movement: Movement): Long

    suspend fun deleteMovement(movementId: Long)
}