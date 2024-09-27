package researchstack.wearable.standalone.data.local.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import researchstack.domain.model.UserProfile

class UserProfilePref(private val userPreferencesStore: DataStore<Preferences>) {
    private val gson = Gson()

    suspend fun save(userProfile: UserProfile) {
        userPreferencesStore.edit { preferences ->
            preferences[PROFILE_KEY] = gson.toJson(userProfile)
        }
    }

    fun getUserProfile(): Flow<UserProfile?> {
        return userPreferencesStore.data.map { preferences ->
            preferences[PROFILE_KEY]?.let {
                gson.fromJson(it, UserProfile::class.java)
            }
        }
    }

    companion object {
        private val PROFILE_KEY = stringPreferencesKey("profile_key")
    }
}
