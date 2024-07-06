package net.martinlundberg.a1repmaxtracker.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Singleton

@Singleton
class WeightUnitService {

    private val _weightUnitFlow: MutableStateFlow<String> = MutableStateFlow("kg")
    val weightUnitFlow: StateFlow<String> = _weightUnitFlow.asStateFlow()

    fun setWeightUnitToPounds(usePounds: Boolean) {
        weightUnit = if (usePounds) "lb" else "kg"

        _weightUnitFlow.update { weightUnit }

        // TODO: Store in Preferences DataStore
        // 1 kg = 2.205 lb
    }

    companion object {
        var weightUnit = "kg" // TODO Should come from Preferences DataStore
    }
}