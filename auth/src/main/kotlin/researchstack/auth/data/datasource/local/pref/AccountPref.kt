package researchstack.auth.data.datasource.local.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import researchstack.auth.domain.model.Account

class AccountPref(private val dataStore: DataStore<Preferences>) {
    suspend fun getAccount(): Account? = dataStore.data.firstOrNull()
        ?.let {
            val id = it[ACCOUNT_ID_PREF_KEY] ?: return@let null
            val provider = it[ACCOUNT_PROVIDER_PREF_KEY] ?: return@let null
            Account(id, provider)
        }

    suspend fun updateAccount(account: Account) {
        dataStore.edit { preferences ->
            preferences[ACCOUNT_ID_PREF_KEY] = account.id
            preferences[ACCOUNT_PROVIDER_PREF_KEY] = account.provider
        }
    }

    suspend fun clearAccount() = dataStore.edit {
        it.remove(ACCOUNT_ID_PREF_KEY)
        it.remove(ACCOUNT_PROVIDER_PREF_KEY)
    }

    companion object {
        private val ACCOUNT_ID_PREF_KEY = stringPreferencesKey("id")
        private val ACCOUNT_PROVIDER_PREF_KEY = stringPreferencesKey("provider")
    }
}
