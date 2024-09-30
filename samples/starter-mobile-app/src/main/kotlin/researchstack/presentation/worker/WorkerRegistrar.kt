package researchstack.presentation.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import researchstack.BuildConfig
import java.util.concurrent.TimeUnit

object WorkerRegistrar {
    private const val period = BuildConfig.DATA_SYNC_PERIOD

    fun registerAllPeriodicWorkers(context: Context) {
        registerSendLogWorker(context)
        registerFetchStudyStatusWorker(context)
        registerHealthDataWorker(context)
        registerUploadFileWorker(context)
        registerWatchConnectedEventWorker(context)
        registerUploadHealthDataFileWorker(context)
    }

    fun registerSendLogWorker(context: Context) {
        registerUniquePeriodicWorker(
            context,
            "SendLogWorker",
            SendLogWorker::class.java,
            60,
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
    }

    fun registerFetchStudyStatusWorker(context: Context) {
        registerUniquePeriodicWorker(
            context,
            "FetchStudyStatusWorker",
            FetchStudyStatusWorker::class.java,
            60,
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
    }

    fun registerWatchConnectedEventWorker(context: Context) {
        registerUniquePeriodicWorker(
            context,
            "WatchConnectedEventWorker",
            WatchConnectedEventWorker::class.java,
            60,
        )
    }

    fun registerOneTimeDataSyncWorker(context: Context) {
        registerUniqueOneTimeWorker(
            context,
            "SyncDataWorker",
            SyncDataWorker::class.java
        )
        registerUniqueOneTimeWorker(
            context,
            "UploadHealthDataFileWorker",
            UploadHealthDataFileWorker::class.java
        )
    }

    fun registerDataSyncWorker(context: Context) {
        registerHealthDataWorker(context)
        registerUploadFileWorker(context)
        registerSendLogWorker(context)
        registerUploadHealthDataFileWorker(context)
    }

    fun registerHealthDataWorker(context: Context) {
        registerUniquePeriodicWorker(
            context,
            "SyncDataWorker",
            SyncDataWorker::class.java,
            period,
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
    }

    fun registerUploadFileWorker(context: Context) {
        registerUniquePeriodicWorker(
            context,
            "UploadFileWorker",
            UploadFileWorker::class.java,
            period,
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
    }

    fun registerUploadHealthDataFileWorker(context: Context) {
        registerUniquePeriodicWorker(
            context,
            "UploadHealthDataFileWorker",
            UploadHealthDataFileWorker::class.java,
            period,
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
    }

    private fun <T : ListenableWorker> registerUniquePeriodicWorker(
        context: Context,
        uniqueWorkName: String,
        workerClass: Class<T>,
        periodInMinute: Long,
        constraints: Constraints? = null,
    ) {
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                uniqueWorkName,
                ExistingPeriodicWorkPolicy.UPDATE,
                PeriodicWorkRequest.Builder(
                    workerClass, periodInMinute, TimeUnit.MINUTES,
                ).apply {
                    constraints?.let {
                        setConstraints(it)
                    }
                }.build()
            )
    }

    private fun <T : ListenableWorker> registerUniqueOneTimeWorker(
        context: Context,
        uniqueWorkName: String,
        workerClass: Class<T>,
    ) {
        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                "$uniqueWorkName-onetime",
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.Builder(workerClass).build()
            )
    }
}
