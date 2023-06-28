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
/**
 * A class that provides access to the meta data stored in the [metaDataStore].
 *
 * @property context The [Context] used to access the [metaDataStore].
 */
class MetaDataStore(val context: Context) {
    private val metaDataStore = context.metaDataStore

    /**
     * Retrieves the changes token for the specified [healthDataTypeString] from the [metaDataStore].
     *
     * @param healthDataTypeString The name of the health data type for which to retrieve the changes token.
     * @return The changes token, or null if it is not present in the [metaDataStore].
     */
    suspend fun readChangesToken(healthDataTypeString: String): String? {
        return metaDataStore.data.map {
            it[stringPreferencesKey(healthDataTypeString)]
        }.first()
    }

    /**
     * Saves the changes token for the specified [healthDataTypeString] to the [metaDataStore].
     *
     * @param healthDataTypeString The name of the health data type for which to save the changes token.
     * @param changesToken The changes token to save.
     */

    /**
     * Saves the changes token for the specified [healthDataTypeString] to the [metaDataStore].
     *
     * @param healthDataTypeString The name of the health data type for which to save the changes token.
     * @param changesToken The changes token to save.
     */

    suspend fun saveChangesToken(healthDataTypeString: String, changesToken: String) {
        metaDataStore.edit {
            it[stringPreferencesKey(healthDataTypeString)] = changesToken
        }
    }

    /**
     * Clears all data from the [metaDataStore].
     */

    suspend fun clearDataStore() {
        metaDataStore.edit {
            it.clear()
        }
    }

    // TODO: store & get loginHistory (Boolean) - (to start workManager or not)
}
