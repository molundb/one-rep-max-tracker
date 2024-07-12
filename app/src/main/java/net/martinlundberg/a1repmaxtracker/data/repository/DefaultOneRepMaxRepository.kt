package net.martinlundberg.a1repmaxtracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import net.martinlundberg.a1repmaxtracker.data.database.dao.OneRMDao
import net.martinlundberg.a1repmaxtracker.data.database.model.asExternalModel
import net.martinlundberg.a1repmaxtracker.data.database.model.asExternalMovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo
import net.martinlundberg.a1repmaxtracker.data.model.asEntity
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService.Companion.poundsToKilos
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService.WeightUnit
import javax.inject.Inject

class DefaultOneRepMaxRepository @Inject constructor(
    private val oneRMDao: OneRMDao,
    private val weightUnitService: WeightUnitService,
) : OneRepMaxRepository {

    override suspend fun getMovementDetail(id: Long): Flow<MovementDetail> =
        oneRMDao.getOneRMsForMovement(id).map { it.asExternalMovementDetail() }

    override suspend fun addOneRM(oneRM: OneRMInfo, weightUnit: WeightUnit) {
        val weight = if (weightUnit.isPounds()) {
            oneRM.weight.poundsToKilos().toFloat()
        } else {
            oneRM.weight
        }
        oneRMDao.insert(oneRM.copy(weight = weight).asEntity())
    }

    override suspend fun getOneRM(id: Long): Flow<OneRMInfo> =
        oneRMDao.getOneRM(id).filterNotNull().map { it.asExternalModel() }

    override suspend fun deleteOneRM(id: Long) = oneRMDao.deleteByOneRMId(id)

    override fun getWeightUnitFlow(): StateFlow<WeightUnit> = weightUnitService.weightUnitFlow

    override suspend fun setWeightUnit(isPounds: Boolean) = weightUnitService.setWeightUnit(isPounds)
}