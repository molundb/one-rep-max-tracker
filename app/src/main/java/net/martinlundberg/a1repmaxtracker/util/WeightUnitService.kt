package net.martinlundberg.a1repmaxtracker.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeightUnitService @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    private val _weightUnitFlow: MutableStateFlow<WeightUnit> = MutableStateFlow(WeightUnit.KILOGRAMS)
    val weightUnitFlow: StateFlow<WeightUnit> = _weightUnitFlow.asStateFlow()

    fun setWeightUnitToPounds(usePounds: Boolean) {
        val weightUnit = if (usePounds) WeightUnit.POUNDS else WeightUnit.KILOGRAMS
        _weightUnitFlow.update { weightUnit }

        // TODO: Store in Preferences DataStore
    }

    companion object {
        private const val KILOS_TO_POUNDS_RATIO = 2.205f

        /*
        Round pounds and kilograms to nearest quarter.
        */
        fun Float.weightWithUnit(isPounds: Boolean) =
            if (isPounds) {
                "${(this * KILOS_TO_POUNDS_RATIO).roundToNearestQuarter().toStringWithoutTrailingZero()} lb"
            } else {
                "${this.roundToNearestQuarter().toStringWithoutTrailingZero()} kg"
            }

        fun Float.poundsToKilos() = (this / KILOS_TO_POUNDS_RATIO).toStringWithoutTrailingZero()
        fun Float.kilosToPounds() = (this * KILOS_TO_POUNDS_RATIO).toStringWithoutTrailingZero()
    }

    enum class WeightUnit {
        KILOGRAMS,
        POUNDS;

        fun isPounds() = this == POUNDS

        override fun toString() = when (this) {
            KILOGRAMS -> "kg"
            POUNDS -> "lb"
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