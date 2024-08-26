package net.martinlundberg.onerepmaxtracker.util

import android.content.Context
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.martinlundberg.onerepmaxtracker.R
import net.martinlundberg.onerepmaxtracker.data.DataStorePreferences
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitRepository.WeightUnit
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitRepository.WeightUnit.KILOGRAMS
import net.martinlundberg.onerepmaxtracker.util.DefaultWeightUnitRepository.WeightUnit.POUNDS
import javax.inject.Inject
import javax.inject.Singleton

interface WeightUnitRepository {
    val weightUnitFlow: StateFlow<WeightUnit>

    suspend fun setWeightUnit(isPounds: Boolean)
}

@Singleton
class DefaultWeightUnitRepository @Inject constructor(
    private val dataStorePreferences: DataStorePreferences,
) : WeightUnitRepository {
    private val scope = MainScope()

    private val _weightUnitFlow: MutableStateFlow<WeightUnit> = MutableStateFlow(KILOGRAMS)
    override val weightUnitFlow: StateFlow<WeightUnit> = _weightUnitFlow.asStateFlow()

    init {
        scope.launch {
            collectWeightUnit()
        }
    }

    private suspend fun collectWeightUnit() {
        dataStorePreferences.weightUnitFlow.collect {
            _weightUnitFlow.value = if (it) POUNDS else KILOGRAMS
        }
    }

    override suspend fun setWeightUnit(isPounds: Boolean) = dataStorePreferences.storeWeightUnit(isPounds)

    companion object {
        private const val KILOS_TO_POUNDS_RATIO = 2.205f

        /*
        Round multiply if pounds and round to the nearest quarter.
        */
        fun Float.multiplyIfPoundsAndRoundToNearestQuarter(weightUnit: WeightUnit) =
            multiplyIfPounds(weightUnit).roundToNearestQuarter()
                .removeTrailingZeros()

        fun Float.multiplyIfPounds(weightUnit: WeightUnit) =
            if (weightUnit == POUNDS) {
                (this * KILOS_TO_POUNDS_RATIO)
            } else {
                this
            }

        fun Float.divideIfPounds(weightUnit: WeightUnit) =
            if (weightUnit == POUNDS) {
                this / KILOS_TO_POUNDS_RATIO
            } else {
                this
            }
    }

    enum class WeightUnit {
        KILOGRAMS,
        POUNDS;

        fun isPounds() = this == POUNDS

        fun toString(context: Context) = when (this) {
            KILOGRAMS -> context.getString(R.string.weight_unit_kilograms)
            POUNDS -> context.getString(R.string.weight_unit_pounds)
        }
    }
}