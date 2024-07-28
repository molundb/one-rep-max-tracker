package net.martinlundberg.onerepmaxtracker.fakes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.martinlundberg.onerepmaxtracker.data.model.MovementDetail
import net.martinlundberg.onerepmaxtracker.data.model.Result
import net.martinlundberg.onerepmaxtracker.data.repository.ResultRepository
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit.KILOGRAMS

class FakeResultRepository : ResultRepository {

    // Change this to Pair<Int, MovementDetail> if need to support having more than 1 MovementDetail
    private val _movementDetail = MutableStateFlow<MovementDetail?>(null)
    private val movementDetail: Flow<MovementDetail?> = _movementDetail

    private val _results = MutableStateFlow<List<Result>>(emptyList())
    val results: Flow<List<Result>> = _results

    private val _analyticsCollectionEnabled = MutableStateFlow(false)
    private val _weightUnit = MutableStateFlow(KILOGRAMS)

    override suspend fun getMovementDetail(id: Long): Flow<MovementDetail?> = movementDetail

    override suspend fun setResult(result: Result, weightUnit: WeightUnit) {
        val results = _movementDetail.value?.results?.filter { it.id != result.id } ?: emptyList()
        val updatedResults = results + result
        _results.emit(updatedResults)

        _movementDetail.tryEmit(
            _movementDetail.value?.copy(
                results = updatedResults
            )
        )
    }

    override suspend fun getResult(id: Long): Flow<Result> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteResult(id: Long) {
        TODO("Not yet implemented")
    }

    override fun getWeightUnitFlow(): StateFlow<WeightUnit> {
        return _weightUnit
    }

    override suspend fun setWeightUnit(isPounds: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getAnalyticsCollectionEnabledFlow(): StateFlow<Boolean> {
        return _analyticsCollectionEnabled
    }

    override suspend fun setAnalyticsCollectionEnabled(isEnabled: Boolean) {
        _analyticsCollectionEnabled.value = isEnabled
    }

    /**
     * Used for testing
     */
    fun setMovementDetail(movementDetail: MovementDetail) {
        _movementDetail.tryEmit(movementDetail)
    }
}