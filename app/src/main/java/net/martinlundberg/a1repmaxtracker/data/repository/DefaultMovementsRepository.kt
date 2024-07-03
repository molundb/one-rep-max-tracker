package net.martinlundberg.a1repmaxtracker.data.repository

import net.martinlundberg.a1repmaxtracker.data.database.dao.MovementDao
import net.martinlundberg.a1repmaxtracker.data.database.dao.OneRMDao
import net.martinlundberg.a1repmaxtracker.data.database.model.asExternalModel
import net.martinlundberg.a1repmaxtracker.data.database.model.asExternalMovement
import net.martinlundberg.a1repmaxtracker.data.database.model.asExternalMovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.Movement
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo
import net.martinlundberg.a1repmaxtracker.data.model.asEntity
import javax.inject.Inject

class DefaultMovementsRepository @Inject constructor(
    private val movementDao: MovementDao,
    private val oneRMDao: OneRMDao,
) : MovementsRepository {
    override suspend fun getMovements(): List<Movement> =
        movementDao.getMovements().entries.map {
            it.asExternalMovement()
        }

    override suspend fun getMovementDetail(id: Long): MovementDetail =
        movementDao.getMovement(id).asExternalMovementDetail()

    override suspend fun setMovement(movement: Movement) = movementDao.insert(movement.asEntity())

    override suspend fun deleteMovement(id: Long) = movementDao.deleteById(id)

    override suspend fun addOneRM(oneRM: OneRMInfo) = oneRMDao.insert(oneRM.asEntity())

    override suspend fun getOneRM(id: Long): OneRMInfo = oneRMDao.getOneRM(id).asExternalModel()
}