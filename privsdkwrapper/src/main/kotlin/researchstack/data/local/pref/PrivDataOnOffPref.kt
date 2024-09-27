package researchstack.data.local.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import researchstack.domain.model.priv.PrivDataType

class PrivDataOnOffPref(dataStore: DataStore<Preferences>, private val prefKey: Preferences.Key<String>) {
    private val gson = Gson()

    private val userPreferencesStore = dataStore
    val privDataTypesFlow: Flow<List<PrivDataType>> = userPreferencesStore.data
        .map { preferences ->
            preferences[prefKey]?.let {
                gson.fromJson(it, Array<PrivDataType>::class.java).toList()
            } ?: emptyList()
        }

    suspend fun add(privDataType: PrivDataType) {
        userPreferencesStore.edit { preferences ->
            val privDataTypes = privDataTypesFlow.first().toMutableList()
            if (!privDataTypes.contains(privDataType)) privDataTypes.add(privDataType)
            preferences[prefKey] = gson.toJson(privDataTypes)
        }
    }

    suspend fun remove(privDataType: PrivDataType) {
        userPreferencesStore.edit { preferences ->
            val privDataTypes = privDataTypesFlow.first().toMutableList()
            if (privDataTypes.contains(privDataType)) privDataTypes.remove(privDataType)
            preferences[prefKey] = gson.toJson(privDataTypes)
        }
    }

    suspend fun clear() = userPreferencesStore.edit { it.remove(prefKey) }

    companion object {
        val PERMITTED_DATA_PREF_KEY = stringPreferencesKey("permitted_data_pref")
        val PASSIVE_ON_OFF_PREF_KEY = stringPreferencesKey("passive_on_off")
    }
}
