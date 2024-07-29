package net.martinlundberg.onerepmaxtracker.data.repository

import kotlinx.coroutines.flow.Flow
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import net.martinlundberg.onerepmaxtracker.feature.movementlist.LatestOrBestResults

interface MovementsRepository {

    val movements: Flow<List<Movement>>

//    suspend fun getMovements(latestOrBestResults: LatestOrBestResults)

    suspend fun setMovement(movement: Movement): Long

    suspend fun deleteMovement(movementId: Long)

    val latestOrBestResults: Flow<LatestOrBestResults>

    suspend fun setLatestOrBestResults(latestOrBestResults: LatestOrBestResults)
}