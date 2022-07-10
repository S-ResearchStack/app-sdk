package com.samsung.healthcare.kit.external.background

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class SyncManager private constructor(
    context: Context,
    private val syncSpecs: List<HealthDataSyncSpec>,
) {
    companion object {
        const val HEALTH_DATA_TYPE_KEY = "type"

        private lateinit var INSTANCE: SyncManager

        fun initialize(context: Context, syncSpecs: List<HealthDataSyncSpec>) {
            synchronized(this) {
                if (::INSTANCE.isInitialized.not()) {
                    INSTANCE = SyncManager(context, syncSpecs)
                }
            }
        }

        fun getInstance(): SyncManager = INSTANCE
    }

    private val workManager = WorkManager.getInstance(context)

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
        .setRequiresBatteryNotLow(false)
        .setRequiresCharging(false)
        .setRequiresDeviceIdle(false)
        .setRequiresStorageNotLow(false)
        .build()

    private fun createInputData(data: Map<String, String>): Data {
        return Data.Builder()
            .putAll(data)
            .build()
    }

    fun startBackgroundSync() {
        syncSpecs.forEach {
            val workRequest = PeriodicWorkRequestBuilder<SyncWorker>(
                it.syncInterval, it.syncTimeUnit,
            )
                .setConstraints(constraints)
                .setInputData(
                    createInputData(
                        mapOf(
                            HEALTH_DATA_TYPE_KEY to it.healthDataTypeString
                        )
                    )
                )
                .build()

            workManager.enqueueUniquePeriodicWork(
                it.healthDataTypeString,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        }
    }

    data class HealthDataSyncSpec(
        val healthDataTypeString: String,
        val syncInterval: Long,
        val syncTimeUnit: TimeUnit,
    )
}
