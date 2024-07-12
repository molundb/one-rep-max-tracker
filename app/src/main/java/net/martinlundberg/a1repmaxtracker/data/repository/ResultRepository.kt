package net.martinlundberg.a1repmaxtracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import net.martinlundberg.a1repmaxtracker.data.model.MovementDetail
import net.martinlundberg.a1repmaxtracker.data.model.Result
import net.martinlundberg.a1repmaxtracker.util.WeightUnitService.WeightUnit

interface ResultRepository {
    suspend fun getMovementDetail(id: Long): Flow<MovementDetail>
    suspend fun addResult(result: Result, weightUnit: WeightUnit)
    suspend fun getResult(id: Long): Flow<Result>
    suspend fun deleteResult(id: Long)
    fun getWeightUnitFlow(): StateFlow<WeightUnit>
    suspend fun setWeightUnit(isPounds: Boolean)
}