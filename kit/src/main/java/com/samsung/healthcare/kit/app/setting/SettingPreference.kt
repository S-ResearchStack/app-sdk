package com.samsung.healthcare.kit.app.setting

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.samsung.healthcare.kit.app.AppStage
import com.samsung.healthcare.kit.app.AppStage.Onboarding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("settings_pref")

class SettingPreference(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val APP_STAGE = intPreferencesKey("app_stage")
    }

    val appStage: Flow<AppStage> = dataStore.data
        .map { preferences ->
            preferences[APP_STAGE]?.let {
                AppStage.values()[it]
            } ?: Onboarding
        }

    suspend fun setAppStage(stage: AppStage) {
        dataStore.edit { pref ->
            pref[APP_STAGE] = stage.ordinal
        }
    }
}
