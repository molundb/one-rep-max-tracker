package net.martinlundberg.a1repmaxtracker.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeightUnitService @Inject constructor() {

    private val _weightUnitFlow: MutableStateFlow<String> = MutableStateFlow("kg")
    val weightUnitFlow: StateFlow<String> = _weightUnitFlow.asStateFlow()

    fun setWeightUnitToPounds(usePounds: Boolean) {
        val weightUnit = if (usePounds) "lb" else "kg"
        _weightUnitFlow.update { weightUnit }

        // TODO: Store in Preferences DataStore
    }

    companion object {
        private const val KILOS_TO_POUNDS_RATIO = 2.205f

        /*
        Round pounds to nearest quarter pound.
        Round kilograms to nearest quarter kilo.
         */
        fun Float.weightWithUnit(isPounds: Boolean) =
            if (isPounds) {
                "${(this * KILOS_TO_POUNDS_RATIO).roundToNearestQuarter().toStringWithoutTrailingZero()} lb"
            } else {
                "${this.roundToNearestQuarter().toStringWithoutTrailingZero()} kg"
            }

        fun Float.poundsToKilos() = (this / KILOS_TO_POUNDS_RATIO).toStringWithoutTrailingZero()

        fun Float.toStringWithoutTrailingZero() = if (this == this.toInt().toFloat()) {
            this.toInt().toString()
        } else {
            this.toString()
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