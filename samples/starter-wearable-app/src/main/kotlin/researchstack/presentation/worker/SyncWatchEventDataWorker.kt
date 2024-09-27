package researchstack.presentation.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import researchstack.domain.usecase.SyncWatchEventDataUseCase

@HiltWorker
class SyncWatchEventDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncWatchEventDataUseCase: SyncWatchEventDataUseCase,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        syncWatchEventDataUseCase().onFailure {
            Log.e(TAG, it.stackTraceToString())
            Log.e(TAG, it.message ?: "")
            return@withContext Result.failure()
        }

        return@withContext Result.success()
    }

    companion object {
        private val TAG = SyncWatchEventDataWorker::class.simpleName
    }
}
