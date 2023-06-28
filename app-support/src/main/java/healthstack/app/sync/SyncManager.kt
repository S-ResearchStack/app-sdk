package healthstack.app.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import healthstack.app.sync.SyncManager.HealthDataSyncSpec
import healthstack.kit.annotation.ForVerificationGenerated
import java.util.concurrent.TimeUnit

/**
 * SyncManager is a singleton class using Android's WorkManager for scheduled health data synchronization tasks.
 * Each task is specified by a [HealthDataSyncSpec], which includes the health data type and its sync interval.
 *
 * @property context The application context.
 * @property syncSpecs The list of health data sync specifications.
 * @constructor Private to enforce the singleton pattern.
 */
@ForVerificationGenerated
class SyncManager private constructor(
    context: Context,
    private val syncSpecs: List<HealthDataSyncSpec>,
) {
    companion object {
        const val HEALTH_DATA_TYPE_KEY = "type"

        private lateinit var INSTANCE: SyncManager

        /**
         * Initializes the [SyncManager] instance with the given [Context] and list of [HealthDataSyncSpec]s.
         * @param context The application context.
         * @param syncSpecs A list of [HealthDataSyncSpec] objects that specify the health data to synchronize.
         */
        fun initialize(context: Context, syncSpecs: List<HealthDataSyncSpec>) {
            synchronized(this) {
                if (Companion::INSTANCE.isInitialized.not()) {
                    INSTANCE = SyncManager(context, syncSpecs)
                }
            }
        }

        /**
         * Returns the [SyncManager] instance.
         * @return The [SyncManager] instance.
         */
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

    /**
     * Creates input data from a [Map] of key-value pairs.
     * @param data A [Map] of key-value pairs to include in the input data.
     * @return The [Data] object representing the input data.
     */

    private fun createInputData(data: Map<String, String>): Data {
        return Data.Builder()
            .putAll(data)
            .build()
    }

    /**
     * Starts the background synchronization for the specified health data types.
     */
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

    /**
     * A data class representing a specification for health data synchronization.
     * @property healthDataTypeString The health data type to synchronize.
     * @property syncInterval The synchronization interval.
     * @property syncTimeUnit The [TimeUnit] for the synchronization interval.
     */
    data class HealthDataSyncSpec(
        val healthDataTypeString: String,
        val syncInterval: Long,
        val syncTimeUnit: TimeUnit,
    )
}
