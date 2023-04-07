package healthstack.kit.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.profileDataStore by preferencesDataStore(name = "settings")

class PreferenceDataStore(val context: Context) {
    companion object {
        val PROFILE = stringPreferencesKey("profile")
    }

    val profile: Flow<String> = context.profileDataStore.data
        .map {
            it[PROFILE] ?: ""
        }

    suspend fun setProfile(profile: String) {
        context.profileDataStore.edit {
            it[PROFILE] = profile
        }
    }
}
