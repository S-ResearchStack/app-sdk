package researchstack.auth.data.datasource.local.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import researchstack.auth.domain.model.BasicAuthentication

class BasicAuthenticationPref(private val dataStore: DataStore<Preferences>) {

    suspend fun getAuthInfo(): BasicAuthentication? = dataStore.data.firstOrNull()
        ?.let { pref ->
            getAuthInfoFromPref(pref)
        }

    private fun getAuthInfoFromPref(pref: Preferences): BasicAuthentication? =
        pref[KEY_ID]?.let { id ->
            pref[KEY_PASSWORD]?.let { password ->
                BasicAuthentication(id, password)
            }
        }

    suspend fun saveAuthInfo(authInfo: BasicAuthentication) {
        dataStore.edit { preferences ->
            preferences[KEY_ID] = authInfo.id
            preferences[KEY_PASSWORD] = authInfo.password
        }
    }

    suspend fun clearAuthInfo() = dataStore.edit {
        it.remove(KEY_ID)
        it.remove(KEY_PASSWORD)
    }

    companion object {
        private val KEY_ID = stringPreferencesKey("auth_info_id")
        private val KEY_PASSWORD = stringPreferencesKey("auth_info_password")
    }
}
