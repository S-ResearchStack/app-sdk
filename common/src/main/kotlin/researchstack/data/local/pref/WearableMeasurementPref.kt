package researchstack.data.local.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WearableMeasurementPref(private val dataStore: DataStore<Preferences>) {
    val ecgMeasurementEnabled: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[ECG_MEASUREMENT_ENABLED] ?: true
        }

    suspend fun setEcgMeasurementEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[ECG_MEASUREMENT_ENABLED] = enabled
        }
    }

    companion object {
        private val ECG_MEASUREMENT_ENABLED = booleanPreferencesKey("ecg_measurement_enabled")
    }
}
