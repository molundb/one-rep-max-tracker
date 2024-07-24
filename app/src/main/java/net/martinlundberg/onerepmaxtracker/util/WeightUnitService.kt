package net.martinlundberg.onerepmaxtracker.util

import android.content.Context
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.martinlundberg.onerepmaxtracker.R
import net.martinlundberg.onerepmaxtracker.data.DataStorePreferences
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit
import net.martinlundberg.onerepmaxtracker.util.WeightUnitServiceImpl.WeightUnit.POUNDS
import javax.inject.Inject
import javax.inject.Singleton

interface WeightUnitService {
    val weightUnitFlow: StateFlow<WeightUnit>

    suspend fun setWeightUnit(isPounds: Boolean)
}

@Singleton
class WeightUnitServiceImpl @Inject constructor(
    private val dataStorePreferences: DataStorePreferences,
) : WeightUnitService {
    private val scope = MainScope()

    private val _weightUnitFlow: MutableStateFlow<WeightUnit> = MutableStateFlow(WeightUnit.KILOGRAMS)
    override val weightUnitFlow: StateFlow<WeightUnit> = _weightUnitFlow.asStateFlow()

    init {
        scope.launch {
            collectWeightUnit()
        }
    }

    private suspend fun collectWeightUnit() {
        dataStorePreferences.weightUnitFlow.collect {
            _weightUnitFlow.value = if (it) WeightUnit.POUNDS else WeightUnit.KILOGRAMS
        }
    }

    override suspend fun setWeightUnit(isPounds: Boolean) = dataStorePreferences.storeWeightUnit(isPounds)

    companion object {
        private const val KILOS_TO_POUNDS_RATIO = 2.205f

        /*
        Round pounds and kilograms to nearest quarter.
        */
        fun Float.weightWithUnit(isPounds: Boolean, context: Context) =
            if (isPounds) {
                "${
                    (this * KILOS_TO_POUNDS_RATIO)
                        .roundToNearestQuarter()
                        .toStringWithoutTrailingZero()
                } ${context.getString(R.string.weight_unit_pounds)}"
            } else {
                "${
                    this
                        .roundToNearestQuarter()
                        .toStringWithoutTrailingZero()
                } ${context.getString(R.string.weight_unit_kilograms)}"
            }

        fun Int.weightWithUnit(weightUnit: WeightUnit, context: Context): String {
            val weight = if (weightUnit == POUNDS) (this * KILOS_TO_POUNDS_RATIO).toInt() else this
            return context.getString(R.string.weight_with_unit, weight, weightUnit.toString(context))

//        return if (isPounds)
//        {
//            "${
//                (this * KILOS_TO_POUNDS_RATIO)
//            } ${context.getString(R.string.weight_unit_pounds)}"
//        } else
//        {
//            "$this ${context.getString(R.string.weight_unit_kilograms)}"
//        }
        }

        fun Float.poundsToKilos() = (this / KILOS_TO_POUNDS_RATIO).toStringWithoutTrailingZero()
        fun Float.kilosToPounds() = (this * KILOS_TO_POUNDS_RATIO).toStringWithoutTrailingZero()
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