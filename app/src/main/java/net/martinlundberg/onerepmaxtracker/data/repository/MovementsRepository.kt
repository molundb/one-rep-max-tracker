package net.martinlundberg.onerepmaxtracker.data.repository

import kotlinx.coroutines.flow.Flow
import net.martinlundberg.onerepmaxtracker.data.model.Movement

interface MovementsRepository {

    val movements: Flow<List<Movement>>

    suspend fun setMovement(movement: Movement): Long

    suspend fun deleteMovement(movementId: Long)
}