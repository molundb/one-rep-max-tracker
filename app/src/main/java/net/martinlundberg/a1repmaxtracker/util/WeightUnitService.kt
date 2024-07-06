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
        weightUnit = if (usePounds) "lb" else "kg"

        _weightUnitFlow.update { weightUnit }

        // TODO: Store in Preferences DataStore
    }

    companion object {
        var weightUnit = "kg" // TODO Should come from Preferences DataStore

        /*
        Round pounds to nearest quarter pound.
        Round kilograms to nearest quarter kilo.
         */
        fun Int.weightWithUnit(isPounds: Boolean) =
            if (isPounds) {
                "${(this * 2.205).roundToNearestQuarter()} lb"
            } else {
                "$this kg"
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