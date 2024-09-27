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
import researchstack.domain.model.log.AppLog
import researchstack.domain.usecase.log.CollectAppLogUseCase
import researchstack.domain.usecase.task.FetchMyTasksUseCase

@HiltWorker
class FetchMyTasksWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val fetchMyTasksUseCase: FetchMyTasksUseCase,
    private val collectAppLogUseCase: CollectAppLogUseCase
) : CoroutineWorker(appContext, workerParams) {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                fetchMyTasksUseCase()
                collectAppLogUseCase(
                    AppLog(FetchMyTasksWorker::class.simpleName).also {
                        it.put("isSuccess", "true")
                        it.put("message", "Succeeded to fetch task")
                    }
                )
                Result.success()
            } catch (ex: Exception) {
                Log.e(FetchStudyTasksWorker::class.simpleName, ex.stackTraceToString())
                collectAppLogUseCase(
                    AppLog(FetchMyTasksWorker::class.simpleName).also {
                        it.put("isSuccess", "false")
                        it.put("message", "Failed to fetch task: ${ex.stackTraceToString()}")
                    }
                )
                Result.retry()
            }
        }
    }
}
