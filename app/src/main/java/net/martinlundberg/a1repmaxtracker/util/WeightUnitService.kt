package net.martinlundberg.a1repmaxtracker.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.martinlundberg.a1repmaxtracker.R
import net.martinlundberg.a1repmaxtracker.data.DataStorePreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeightUnitService @Inject constructor(
    private val dataStorePreferences: DataStorePreferences,
) {
    private val scope = MainScope()

    private val _weightUnitFlow: MutableStateFlow<WeightUnit> = MutableStateFlow(WeightUnit.KILOGRAMS)
    val weightUnitFlow: StateFlow<WeightUnit> = _weightUnitFlow.asStateFlow()

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

    suspend fun setWeightUnit(isPounds: Boolean) = dataStorePreferences.storeWeightUnit(isPounds)

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

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WeightUnitServiceEntryPoint {
    fun weightUnitService(): WeightUnitService
}

@Composable
fun provideWeightUnitService(): WeightUnitService {
    val hiltEntryPoint =
        EntryPointAccessors.fromApplication(LocalContext.current, WeightUnitServiceEntryPoint::class.java)
    return hiltEntryPoint.weightUnitService()
}