package net.martinlundberg.a1repmaxtracker.data.repository

import kotlinx.coroutines.flow.Flow
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.OneRMInfo
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService.WeightUnit

interface OneRepMaxRepository {
    suspend fun getMovementDetail(id: Long): Flow<MovementDetail>
    suspend fun addOneRM(oneRM: OneRMInfo, weightUnit: WeightUnit)
    suspend fun getOneRM(id: Long): Flow<OneRMInfo>
    suspend fun deleteOneRM(id: Long)
}