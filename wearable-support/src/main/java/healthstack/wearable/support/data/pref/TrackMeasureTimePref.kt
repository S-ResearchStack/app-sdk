package healthstack.wearable.support.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val APP_PREFERENCES_NAME = "app_prefs"

val Context.dataStore by preferencesDataStore(
    name = APP_PREFERENCES_NAME
)

class TrackMeasureTimePref(private val userPreferencesStore: DataStore<Preferences>) {

    suspend fun add(value: Long) {
        userPreferencesStore.edit { preferences ->
            preferences[prefKey] = value
        }
    }

    fun getLastMeasureFlow(): Flow<Long> =
        userPreferencesStore.data.map {
            it[prefKey] ?: 0
        }

    companion object {
        private val TAG = this::class.simpleName
        private val prefKey = longPreferencesKey("ecg")
    }
}
