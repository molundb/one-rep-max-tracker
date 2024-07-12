package net.martinlundberg.onerepmaxtracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import net.martinlundberg.onerepmaxtracker.data.database.dao.ResultDao
import net.martinlundberg.onerepmaxtracker.data.database.model.asExternalModel
import net.martinlundberg.onerepmaxtracker.data.database.model.asExternalMovementDetail
import net.martinlundberg.onerepmaxtracker.data.model.MovementDetail
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.data.model.asEntity
import net.martinlundberg.onerepmaxtracker.util.WeightUnitService
import net.martinlundberg.onerepmaxtracker.util.WeightUnitService.Companion.poundsToKilos
import net.martinlundberg.onerepmaxtracker.util.WeightUnitService.WeightUnit
import javax.inject.Inject

class DefaultResultRepository @Inject constructor(
    private val resultDao: ResultDao,
    private val weightUnitService: WeightUnitService,
) : ResultRepository {

    override suspend fun getMovementDetail(id: Long): Flow<MovementDetail> =
        resultDao.getResultsForMovement(id).map { it.asExternalMovementDetail() }

    override suspend fun addResult(result: Result, weightUnit: WeightUnit) {
        val weight = if (weightUnit.isPounds()) {
            result.weight.poundsToKilos().toFloat()
        } else {
            result.weight
        }
        resultDao.insert(result.copy(weight = weight).asEntity())
    }

    override suspend fun getResult(id: Long): Flow<Result> =
        resultDao.getResult(id).filterNotNull().map { it.asExternalModel() }

    override suspend fun deleteResult(id: Long) = resultDao.deleteByResultId(id)

    override fun getWeightUnitFlow(): StateFlow<WeightUnit> = weightUnitService.weightUnitFlow

    override suspend fun setWeightUnit(isPounds: Boolean) = weightUnitService.setWeightUnit(isPounds)
}