package net.martinlundberg.onerepmaxtracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsHelper
import net.martinlundberg.onerepmaxtracker.analytics.logDeleteMovement
import net.martinlundberg.onerepmaxtracker.data.database.dao.MovementDao
import net.martinlundberg.onerepmaxtracker.data.database.dao.ResultDao
import net.martinlundberg.onerepmaxtracker.data.database.model.asExternalMovement
import net.martinlundberg.onerepmaxtracker.data.model.Movement
import net.martinlundberg.onerepmaxtracker.data.model.asEntity
import net.martinlundberg.onerepmaxtracker.feature.movementlist.LatestOrBestResults
import net.martinlundberg.onerepmaxtracker.feature.movementlist.LatestOrBestResultsInMovementListScreenService
import javax.inject.Inject

class DefaultMovementsRepository @Inject constructor(
    private val movementDao: MovementDao,
    private val resultDao: ResultDao,
    private val analyticsHelper: AnalyticsHelper,
    private val latestOrBestResultsInMovementListScreenService: LatestOrBestResultsInMovementListScreenService,
) : MovementsRepository {
    override val movements: Flow<List<Movement>> = combine(
        movementDao.getMovements(),
        latestOrBestResultsInMovementListScreenService.latestOrBestResults,
    ) { movements, latest ->
        movements.map {
            it.asExternalMovement(latest)
        }
    }

    override suspend fun setMovement(movement: Movement) = movementDao.insert(movement.asEntity())

    override suspend fun deleteMovement(movementId: Long) {
        analyticsHelper.logDeleteMovement(movementId)
        movementDao.deleteById(movementId)
        resultDao.deleteAllWithMovementId(movementId)
    }

    override val latestOrBestResults: Flow<LatestOrBestResults> =
        latestOrBestResultsInMovementListScreenService.latestOrBestResults

    override suspend fun setLatestOrBestResults(latestOrBestResults: LatestOrBestResults) =
        latestOrBestResultsInMovementListScreenService.setLatestOrBestResults(latestOrBestResults)
}