package researchstack.wearable.standalone.data.local.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SubjectIdPref(private val userPreferencesStore: DataStore<Preferences>) {
    suspend fun save(subjectNumber: String) {
        userPreferencesStore.edit { preferences ->
            preferences[SUBJECT_ID_KEY] = subjectNumber
        }
    }

    fun getSubjectId(): Flow<String?> {
        return userPreferencesStore.data.map { preferences ->
            preferences[SUBJECT_ID_KEY]
        }
    }

    companion object {
        private val SUBJECT_ID_KEY = stringPreferencesKey("subject_id_key")
    }
}
