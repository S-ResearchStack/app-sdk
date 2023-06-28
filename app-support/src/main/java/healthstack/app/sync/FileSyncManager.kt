package healthstack.app.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import healthstack.kit.annotation.ForVerificationGenerated
import java.util.concurrent.TimeUnit.MINUTES

@ForVerificationGenerated
class FileSyncManager private constructor(
    context: Context,
) {
    companion object {
        private lateinit var INSTANCE: FileSyncManager
        private var syncInterval: Long = 15
        fun initialize(context: Context, interval: Long = 15) {
            synchronized(this) {
                if (Companion::INSTANCE.isInitialized.not()) {
                    INSTANCE = FileSyncManager(context)
                    syncInterval = interval
                }
            }
        }

        fun getInstance(): FileSyncManager = INSTANCE
    }

    private val workManager = WorkManager.getInstance(context)

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
        .setRequiresBatteryNotLow(false)
        .setRequiresCharging(false)
        .setRequiresDeviceIdle(false)
        .setRequiresStorageNotLow(false)
        .build()

    fun startBackgroundSync() {
        val workRequest = PeriodicWorkRequestBuilder<FileSyncWorker>(
            syncInterval, MINUTES,
        )
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "FileSync",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
