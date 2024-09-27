package researchstack.data.datasource.local.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull

class SyncTimePref(
    private val dataStore: DataStore<Preferences>,
    syncTimePrefKey: SyncTimePrefKey,
) {
    private val prefKey = stringPreferencesKey(syncTimePrefKey.name)

    suspend fun getLastSyncTime(): Long? = dataStore.data.firstOrNull()?.let { preferences ->
        preferences[prefKey]?.toLong()
    }

    suspend fun update(lastSyncTime: Long) {
        dataStore.edit { preferences -> preferences[prefKey] = lastSyncTime.toString() }
    }

    enum class SyncTimePrefKey {
        USAGE_STATS_SAVE,
        USAGE_STATS_SYNC,
        LIGHT_SYNC,
        SPEED_SYNC,
        ACCELEROMETER_SYNC,
        PLACE_EVENT_SYNC,
        PRESENCE_EVENT_SYNC,
        CALL_LOG_SYNC
    }
}
