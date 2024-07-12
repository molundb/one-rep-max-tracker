package net.martinlundberg.a1repmaxtracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import net.martinlundberg.a1repmaxtracker.data.database.dao.ResultDao
import net.martinlundberg.a1repmaxtracker.data.database.model.asExternalModel
import net.martinlundberg.a1repmaxtracker.data.database.model.asExternalMovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.Result
import net.martinlundberg.a1repmaxtracker.data.model.asEntity
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService.Companion.poundsToKilos
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService.WeightUnit
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