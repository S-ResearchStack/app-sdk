package researchstack.wearable.standalone.data.local.pref

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackMeasureTimePref(private val userPreferencesStore: DataStore<Preferences>) {

    suspend fun add(prefKey: Preferences.Key<Long>, value: Long) {
        userPreferencesStore.edit { preferences ->
            Log.d(TAG, "add: $prefKey")
            preferences[prefKey] = value
        }
    }

    suspend fun getLastMeasureFlow(prefKey: Preferences.Key<Long>): Flow<Long> =
        userPreferencesStore.data.map {
            Log.d(TAG, "get: $prefKey")
            it[prefKey] ?: 0
        }

    companion object {
        private val TAG = this::class.simpleName
    }
}
