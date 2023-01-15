package healthstack.app.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val META_DATA_STORE_NAME = "metaDataStore"

private val Context.metaDataStore: DataStore<Preferences> by preferencesDataStore(
    name = META_DATA_STORE_NAME
)

class MetaDataStore(val context: Context) {
    private val metaDataStore = context.metaDataStore

    suspend fun readChangesToken(healthDataTypeString: String): String? {
        return metaDataStore.data.map {
            it[stringPreferencesKey(healthDataTypeString)]
        }.first()
    }

    suspend fun saveChangesToken(healthDataTypeString: String, changesToken: String) {
        metaDataStore.edit {
            it[stringPreferencesKey(healthDataTypeString)] = changesToken
        }
    }

    suspend fun clearDataStore() {
        metaDataStore.edit {
            it.clear()
        }
    }

    // TODO: store & get loginHistory (Boolean) - (to start workManager or not)
}
