package net.martinlundberg.onerepmaxtracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsEnabledService
import net.martinlundberg.onerepmaxtracker.analytics.AnalyticsService
import net.martinlundberg.onerepmaxtracker.analytics.logAnalyticsEnabledToggled
import net.martinlundberg.onerepmaxtracker.analytics.logDeleteResult
import net.martinlundberg.onerepmaxtracker.data.database.dao.ResultDao
import net.martinlundberg.onerepmaxtracker.data.database.model.asExternalModel
import net.martinlundberg.onerepmaxtracker.data.database.model.asExternalMovementDetail
import net.martinlundberg.onerepmaxtracker.data.model.MovementDetail
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.data.model.asEntity
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitService
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitService.Companion.divideIfPounds
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitService.WeightUnit
import javax.inject.Inject

class DefaultResultRepository @Inject constructor(
    private val resultDao: ResultDao,
    private val weightUnitService: DefaultWeightUnitService,
    private val analyticsEnabledService: AnalyticsEnabledService,
    private val analyticsService: AnalyticsService,
) : ResultRepository {

    override suspend fun getMovementDetail(id: Long): Flow<MovementDetail?> =
        resultDao.getResultsForMovement(id).map { map ->
            map.entries.firstOrNull()?.asExternalMovementDetail()
        }

    override suspend fun setResult(result: Result, weightUnit: WeightUnit) {
        val weight = result.weight.divideIfPounds(weightUnit)
        resultDao.insert(result.copy(weight = weight).asEntity())
    }

    override suspend fun getResult(id: Long): Flow<Result?> =
        resultDao.getResult(id).map { it?.asExternalModel() }

    override suspend fun deleteResult(id: Long) {
        analyticsService.logDeleteResult(id)
        resultDao.deleteByResultId(id)
    }

    override fun getWeightUnitFlow(): StateFlow<WeightUnit> = weightUnitService.weightUnitFlow

    override suspend fun setWeightUnit(isPounds: Boolean) = weightUnitService.setWeightUnit(isPounds)

    //TODO: Create separate analytics collection repo?
    override fun getAnalyticsCollectionEnabledFlow(): StateFlow<Boolean> = analyticsEnabledService.analyticsEnabledFlow

    override suspend fun setAnalyticsCollectionEnabled(isEnabled: Boolean) {
        analyticsService.logAnalyticsEnabledToggled(isEnabled)
        analyticsEnabledService.setAnalyticsCollectionEnabled(isEnabled)
    }
}