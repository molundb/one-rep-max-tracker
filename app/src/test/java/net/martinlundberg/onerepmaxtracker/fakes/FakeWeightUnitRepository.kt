package net.martinlundberg.onerepmaxtracker.fakes

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitRepository.WeightUnit
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitRepository.WeightUnit.KILOGRAMS
import net.martinlundberg.onerepmaxtracker.util.WeightUnitRepository

class FakeWeightUnitRepository : WeightUnitRepository {
    private val _weightUnit = MutableStateFlow(KILOGRAMS)

    override val weightUnitFlow: StateFlow<WeightUnit>
        get() = _weightUnit

    override suspend fun setWeightUnit(isPounds: Boolean) {
        TODO("Not yet implemented")
    }
}