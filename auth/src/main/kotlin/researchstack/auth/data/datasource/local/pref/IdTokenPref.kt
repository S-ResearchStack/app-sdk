package researchstack.auth.data.datasource.local.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import researchstack.auth.domain.model.IdToken

class IdTokenPref(private val dataStore: DataStore<Preferences>) {
    suspend fun getIdToken(): IdToken? = dataStore.data.firstOrNull()
        ?.let {
            val token = it[ID_TOKEN_PREF_KEY] ?: return@let null
            val issuer = it[ISSUER_PREF_KEY] ?: return@let null
            IdToken(token, issuer)
        }

    suspend fun updateIdToken(idToken: IdToken) {
        dataStore.edit { preferences ->
            preferences[ID_TOKEN_PREF_KEY] = idToken.token
            preferences[ISSUER_PREF_KEY] = idToken.issuer
        }
    }

    suspend fun clearIdToken() {
        dataStore.edit {
            it.remove(ID_TOKEN_PREF_KEY)
            it.remove(ISSUER_PREF_KEY)
        }
    }

    companion object {
        private val ID_TOKEN_PREF_KEY = stringPreferencesKey("id_token")
        private val ISSUER_PREF_KEY = stringPreferencesKey("issuer")
    }
}
