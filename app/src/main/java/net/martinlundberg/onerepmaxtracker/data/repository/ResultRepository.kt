package net.martinlundberg.onerepmaxtracker.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import net.martinlundberg.onerepmaxtracker.data.model.MovementDetail
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitService.WeightUnit

interface ResultRepository {
    suspend fun getMovementDetail(id: Long): Flow<MovementDetail?>
    suspend fun setResult(result: Result, weightUnit: WeightUnit)
    suspend fun getResult(id: Long): Flow<Result?>
    suspend fun deleteResult(id: Long)
    fun getWeightUnitFlow(): StateFlow<WeightUnit>
    suspend fun setWeightUnit(isPounds: Boolean)

    //TODO: Create separate repo for this?
    fun getAnalyticsCollectionEnabledFlow(): StateFlow<Boolean>
    suspend fun setAnalyticsCollectionEnabled(isEnabled: Boolean)
}