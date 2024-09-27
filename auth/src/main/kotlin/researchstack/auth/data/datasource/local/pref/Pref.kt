package researchstack.auth.data.datasource.local.pref

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

private const val APP_PREFERENCES_NAME = "auth_prefs"

val Context.dataStore by preferencesDataStore(
    name = APP_PREFERENCES_NAME
)
