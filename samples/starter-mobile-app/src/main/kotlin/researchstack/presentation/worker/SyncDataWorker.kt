package researchstack.presentation.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import researchstack.domain.model.log.AppLog
import researchstack.domain.usecase.healthConnect.SyncHealthConnectDataUseCase
import researchstack.domain.usecase.log.CollectAppLogUseCase
import researchstack.domain.usecase.sensor.SyncTrackerDataUseCase
import researchstack.domain.usecase.wearable.SyncWearableDataUseCase

@HiltWorker
class SyncDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncTrackerDataUseCase: SyncTrackerDataUseCase,
    private val collectAppLogUseCase: CollectAppLogUseCase,
    private val syncWearableDataUseCase: SyncWearableDataUseCase,
    private val syncHealthConnectDataUseCase: SyncHealthConnectDataUseCase,
) : NetworkAwareWorker(appContext, workerParams) {

    override suspend fun doTask(): Result {
        return withContext(Dispatchers.IO) {
            listOf<suspend () -> Unit>(
                { syncTrackerDataUseCase() },
                { syncWearableDataUseCase() },
                { syncHealthConnectDataUseCase() }
            ).forEach { func ->
                CoroutineScope(Dispatchers.IO).launch {
                    kotlin.runCatching { func() }.onFailure {
                        collectAppLogUseCase(
                            AppLog(TAG).also { logData ->
                                logData.put("isSuccess", "false")
                                logData.put(
                                    "message",
                                    "Failed to ${func::class.simpleName}: ${it.stackTraceToString()}"
                                )
                            }
                        )
                        Log.e(TAG, it.stackTraceToString())
                    }.onSuccess {
                        collectAppLogUseCase(
                            AppLog(TAG).also {
                                it.put("isSuccess", "true")
                                it.put("message", "Success to ${func::class.simpleName}")
                            }
                        )
                        Log.i(TAG, "success to sync")
                    }
                }
            }
            Result.success()
        }
    }

    companion object {
        private val TAG = SyncDataWorker::class.simpleName
    }
}
