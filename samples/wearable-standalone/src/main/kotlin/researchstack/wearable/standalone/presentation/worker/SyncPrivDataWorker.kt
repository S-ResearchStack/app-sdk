package researchstack.wearable.standalone.presentation.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import researchstack.wearable.standalone.domain.usecase.SyncPrivDataUseCase

@HiltWorker
class SyncPrivDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncPrivDataUseCase: SyncPrivDataUseCase,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        syncPrivDataUseCase(applicationContext).onFailure {
            Log.e(TAG, it.stackTraceToString())
            Log.e(TAG, it.message ?: "")
            return@withContext Result.failure()
        }

        return@withContext Result.success()
    }

    companion object {
        private val TAG = SyncPrivDataWorker::class.simpleName
    }
}
