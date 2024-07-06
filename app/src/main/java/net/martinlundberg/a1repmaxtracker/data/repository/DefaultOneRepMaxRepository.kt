package net.martinlundberg.a1repmaxtracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import net.martinlundberg.a1repmaxtracker.data.database.dao.OneRMDao
import net.martinlundberg.a1repmaxtracker.data.database.model.asExternalModel
import net.martinlundberg.a1repmaxtracker.data.database.model.asExternalMovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo
import net.martinlundberg.a1repmaxtracker.data.model.asEntity
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService.Companion.poundsToKilos
import javax.inject.Inject

class DefaultOneRepMaxRepository @Inject constructor(
    private val oneRMDao: OneRMDao,
) : OneRepMaxRepository {

    override fun getMovementDetail(id: Long): Flow<MovementDetail> =
        oneRMDao.getOneRMsForMovement(id).map { it.asExternalMovementDetail() }

    override suspend fun addOneRM(oneRM: OneRMInfo, weightUnit: String) {
        val weight = if (weightUnit == "lb") {
            oneRM.weight.poundsToKilos().toFloat()
        } else {
            oneRM.weight
        }
        oneRMDao.insert(oneRM.copy(weight = weight).asEntity())
    }

    override fun getOneRM(id: Long): Flow<OneRMInfo> = oneRMDao.getOneRM(id).map { it.asExternalModel() }

    override suspend fun deleteOneRM(id: Long) = oneRMDao.deleteByOneRMId(id)
}