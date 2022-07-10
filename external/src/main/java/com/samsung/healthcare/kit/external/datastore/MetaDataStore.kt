package com.samsung.healthcare.kit.external.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class MetaDataStore(val context: Context) {

    companion object {
        const val MIN_TIME_STRING = "0000-01-01T00:00:00Z"
        const val META_DATA_STORE_NAME = "metaDataStore"
    }

    private val Context.metaDataStore: DataStore<Preferences> by preferencesDataStore(
        name = META_DATA_STORE_NAME
    )

    private val metaDataStore = context.metaDataStore

    suspend fun readLatestSyncTime(healthDataTypeString: String): String {
        return metaDataStore.data.map {
            it[stringPreferencesKey(healthDataTypeString)] ?: MIN_TIME_STRING
        }.first()
    }

    suspend fun saveLatestSyncTime(healthDataTypeString: String, latestSyncTime: String) {
        metaDataStore.edit {
            it[stringPreferencesKey(healthDataTypeString)] = latestSyncTime
        }
    }

    suspend fun clearDataStore() {
        metaDataStore.edit {
            it.clear()
        }
    }
}
