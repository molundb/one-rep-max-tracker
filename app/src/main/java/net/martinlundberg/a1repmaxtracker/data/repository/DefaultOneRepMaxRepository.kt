package net.martinlundberg.a1repmaxtracker.data.repository

import net.martinlundberg.a1repmaxtracker.data.database.dao.OneRMDao
import net.martinlundberg.a1repmaxtracker.data.database.model.asExternalModel
import net.martinlundberg.a1repmaxtracker.data.database.model.asExternalMovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo
import net.martinlundberg.a1repmaxtracker.data.model.asEntity
import javax.inject.Inject

class DefaultOneRepMaxRepository @Inject constructor(
    private val oneRMDao: OneRMDao,
) : OneRepMaxRepository {

    override suspend fun getMovementDetail(id: Long): MovementDetail =
        oneRMDao.getOneRMsForMovement(id).asExternalMovementDetail()

    override suspend fun addOneRM(oneRM: OneRMInfo) = oneRMDao.insert(oneRM.asEntity())

    override suspend fun getOneRM(id: Long): OneRMInfo = oneRMDao.getOneRM(id).asExternalModel()

    override suspend fun deleteOneRM(id: Long) = oneRMDao.deleteById(id)
}