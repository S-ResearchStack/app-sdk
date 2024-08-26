package healthstack.app.sync

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class SyncWearDataManager private constructor(
    context: Context,
    val syncInterval: Long,
    val syncIntervalTimeUnit: TimeUnit
) {
    companion object {
        private lateinit var INSTANCE: SyncWearDataManager

        fun initialize(context: Context, syncInterval: Long, syncIntervalTimeUnit: TimeUnit) {
            synchronized(this) {
                if (Companion::INSTANCE.isInitialized.not()) {
                    INSTANCE = SyncWearDataManager(context, syncInterval, syncIntervalTimeUnit)
                }
            }
        }

        fun getInstance(): SyncWearDataManager = SyncWearDataManager.INSTANCE
    }

    private val workManager = WorkManager.getInstance(context)

    fun startBackgroundSync() {
        workManager.enqueueUniquePeriodicWork(
            SyncWearDataWorker::class.java.simpleName,
            ExistingPeriodicWorkPolicy.REPLACE,
            PeriodicWorkRequestBuilder<SyncWearDataWorker>(
                syncInterval, syncIntervalTimeUnit,
            ).build(),
        )
    }
}
