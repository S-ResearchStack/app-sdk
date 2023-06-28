package healthstack.app.pref

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import healthstack.app.pref.AppStage.Onboarding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("settings_pref")

/**
 * A class that handles the application settings using the Android data store.
 *
 * @param context The application context
 */

class SettingPreference(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val APP_STAGE = intPreferencesKey("app_stage")
        val TASK_SYNC_TIME = stringPreferencesKey("task_sync_time")
        val TASK_RESULT_SYNC_TIME = stringPreferencesKey("task_result_sync_time")
    }

    val appStage: Flow<AppStage> = dataStore.data
        .map { preferences ->
            preferences[APP_STAGE]?.let {
                AppStage.values()[it]
            } ?: Onboarding
        }

    /**
     * Sets the current application stage to the given [stage].
     *
     * @param stage The new application stage
     */
    suspend fun setAppStage(stage: AppStage) {
        dataStore.edit { pref ->
            pref[APP_STAGE] = stage.ordinal
        }
    }

    val taskSyncTime: Flow<String> = dataStore.data
        .map {
            it[TASK_SYNC_TIME] ?: "2000-01-01T00:00:00.000"
        }

    val taskResultSyncTime: Flow<String> = dataStore.data
        .map {
            it[TASK_RESULT_SYNC_TIME] ?: "2000-01-01T00:00:00.000"
        }

    /**
     * Sets the task synchronization time to the given [taskSyncTime].
     *
     * @param taskSyncTime The new task synchronization time
     */
    suspend fun setTaskSyncTime(taskSyncTime: String) {
        dataStore.edit {
            it[TASK_SYNC_TIME] = taskSyncTime
        }
    }

    suspend fun setTaskResultSyncTime(taskResultSyncTime: String) {
        dataStore.edit {
            it[TASK_RESULT_SYNC_TIME] = taskResultSyncTime
        }
    }
}
