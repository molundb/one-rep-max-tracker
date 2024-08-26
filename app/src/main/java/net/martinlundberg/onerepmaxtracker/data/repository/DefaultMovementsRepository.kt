package net.martinlundberg.onerepmaxtracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsService
import net.martinlundberg.onerepmaxtracker.analytics.logDeleteMovement
import net.martinlundberg.onerepmaxtracker.data.database.dao.MovementDao
import net.martinlundberg.onerepmaxtracker.data.database.dao.ResultDao
import net.martinlundberg.onerepmaxtracker.data.database.model.asExternalMovement
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import net.martinlundberg.onerepmaxtracker.data.model.asEntity
import net.martinlundberg.onerepmaxtracker.feature.movementlist.LatestOrBestResults
import net.martinlundberg.onerepmaxtracker.feature.movementlist.LatestOrBestResultsInMovementListScreenRepository
import javax.inject.Inject

class DefaultMovementsRepository @Inject constructor(
    private val movementDao: MovementDao,
    private val resultDao: ResultDao,
    private val analyticsService: AnalyticsService,
    private val latestOrBestResultsInMovementListScreenRepository: LatestOrBestResultsInMovementListScreenRepository,
) : MovementsRepository {
    override val movements: Flow<List<Movement>> = combine(
        movementDao.getMovements(),
        latestOrBestResultsInMovementListScreenRepository.latestOrBestResults,
    ) { movements, latest ->
        movements.map {
            it.asExternalMovement(latest)
        }
    }

    override suspend fun setMovement(movement: Movement) = movementDao.insert(movement.asEntity())

    override suspend fun deleteMovement(movementId: Long) {
        analyticsService.logDeleteMovement(movementId)
        movementDao.deleteById(movementId)
        resultDao.deleteAllWithMovementId(movementId)
    }

    override val latestOrBestResults: Flow<LatestOrBestResults> =
        latestOrBestResultsInMovementListScreenRepository.latestOrBestResults

    override suspend fun setLatestOrBestResults(latestOrBestResults: LatestOrBestResults) =
        latestOrBestResultsInMovementListScreenRepository.setLatestOrBestResults(latestOrBestResults)
}