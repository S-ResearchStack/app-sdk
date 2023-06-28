package healthstack.kit.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.profileDataStore by preferencesDataStore(name = "settings")

class PreferenceDataStore(val context: Context) {
    companion object {
        val PROFILE = stringPreferencesKey("profile")
        val REMINDER = stringPreferencesKey("reminder")
        val PUSH = booleanPreferencesKey("push")
    }

    val profile: Flow<String> = context.profileDataStore.data
        .map {
            it[PROFILE] ?: ""
        }

    val reminder: Flow<String> = context.profileDataStore.data
        .map {
            it[REMINDER] ?: "06:00 AM"
        }

    val push: Flow<Boolean> = context.profileDataStore.data
        .map {
            it[PUSH] ?: false
        }

    suspend fun setProfile(profile: String) {
        context.profileDataStore.edit {
            it[PROFILE] = profile
        }
    }

    suspend fun setReminder(reminder: String) {
        context.profileDataStore.edit {
            it[REMINDER] = reminder
        }
    }

    suspend fun setPush(push: Boolean) {
        context.profileDataStore.edit {
            it[PUSH] = push
        }
    }
}
