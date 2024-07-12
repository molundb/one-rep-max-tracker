package net.martinlundberg.a1repmaxtracker.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStorePreferences @Inject constructor(
    @ApplicationContext val context: Context,
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private val weightUnitIsPoundsKey = booleanPreferencesKey("weight_unit_is_pounds")
    val weightUnitFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[weightUnitIsPoundsKey] ?: false
    }

    suspend fun storeWeightUnit(isPounds: Boolean) {
        context.dataStore.edit { settings ->
            settings[weightUnitIsPoundsKey] = isPounds
        }
    }
}