package net.martinlundberg.onerepmaxtracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsEnabledService
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsHelper
import net.martinlundberg.onerepmaxtracker.analytics.logDeleteResult
import net.martinlundberg.onerepmaxtracker.data.database.dao.ResultDao
import net.martinlundberg.onerepmaxtracker.data.database.model.asExternalModel
import net.martinlundberg.onerepmaxtracker.data.database.model.asExternalMovementDetail
import net.martinlundberg.onerepmaxtracker.data.model.MovementDetail
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.data.model.asEntity
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.Companion.poundsToKilos
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit
import javax.inject.Inject

class DefaultResultRepository @Inject constructor(
    private val resultDao: ResultDao,
    private val weightUnitService: WeightUnitServiceImpl,
    private val analyticsEnabledService: AnalyticsEnabledService,
    private val analyticsHelper: AnalyticsHelper,
) : ResultRepository {

    override suspend fun getMovementDetail(id: Long): Flow<MovementDetail?> =
        resultDao.getResultsForMovement(id).map { map ->
            map.entries.firstOrNull()?.asExternalMovementDetail()
        }

    override suspend fun setResult(result: Result, weightUnit: WeightUnit) {
        val weight = if (weightUnit.isPounds()) {
            result.weight.poundsToKilos().toFloat()
        } else {
            result.weight
        }
        resultDao.insert(result.copy(weight = weight).asEntity())
    }

    override suspend fun getResult(id: Long): Flow<Result> =
        resultDao.getResult(id).filterNotNull().map { it.asExternalModel() }

    override suspend fun deleteResult(id: Long) {
        analyticsHelper.logDeleteResult(id)
        resultDao.deleteByResultId(id)
    }

    override fun getWeightUnitFlow(): StateFlow<WeightUnit> = weightUnitService.weightUnitFlow

    override suspend fun setWeightUnit(isPounds: Boolean) = weightUnitService.setWeightUnit(isPounds)

    override fun getAnalyticsCollectionEnabledFlow(): StateFlow<Boolean> = analyticsEnabledService.analyticsEnabledFlow

    override suspend fun setAnalyticsCollectionEnabled(isEnabled: Boolean) =
        analyticsEnabledService.setAnalyticsCollectionEnabled(isEnabled)
}