package researchstack.presentation.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import researchstack.BuildConfig

class UIPreference(private val dataStore: DataStore<Preferences>) {
    val logPageConfigFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[LOG_PAGE_PREF_KEY] ?: false
        }

    suspend fun toggleLogPage() {
        dataStore.edit { preferences ->
            preferences[LOG_PAGE_PREF_KEY] = preferences[LOG_PAGE_PREF_KEY]?.let {
                !it
            } ?: true
        }
    }

    val mobileDataSyncEnabled: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[MOBILE_DATA_ENABLED_KEY] ?: BuildConfig.ENABLE_CELLULAR_DATA_TO_SYNC_DATA
        }

    suspend fun setMobileDataSync(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[MOBILE_DATA_ENABLED_KEY] = enabled
        }
    }

    val inClinicModeUntil: Flow<Long> = dataStore.data
        .map { preferences ->
            // NOTE epoch second
            preferences[IN_CLINIC_MODE_UNTIL_KEY] ?: 0L
        }

    suspend fun setInClinicModeUntil(until: Long) {
        dataStore.edit { preferences ->
            preferences[IN_CLINIC_MODE_UNTIL_KEY] = until
        }
    }

    companion object {
        private val LOG_PAGE_PREF_KEY = booleanPreferencesKey("log_page")
        private val MOBILE_DATA_ENABLED_KEY = booleanPreferencesKey("mobile_data_sync")
        private val IN_CLINIC_MODE_UNTIL_KEY = longPreferencesKey("in_clinic_mode_until")
    }
}
