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
import researchstack.domain.usecase.log.SendAppLogUseCase

@HiltWorker
class SendLogWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val sendAppLogUseCase: SendAppLogUseCase,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            kotlin.runCatching {
                sendAppLogUseCase().getOrThrow()
            }.onFailure {
                Log.e(SendLogWorker::class.simpleName, it.stackTraceToString())
            }
            Result.success()
        }
    }
}
