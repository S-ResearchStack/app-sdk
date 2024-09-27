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
import researchstack.domain.usecase.study.FetchStudyUseCase

@HiltWorker
class FetchStudyWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val fetchStudyUseCase: FetchStudyUseCase,
    private val collectAppLogUseCase: CollectAppLogUseCase
) : CoroutineWorker(appContext, workerParams) {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                fetchStudyUseCase()
                collectAppLogUseCase(
                    AppLog(FetchStudyWorker::class.simpleName).also {
                        it.put("isSuccess", "true")
                        it.put("message", "Succeeded to fetch studies")
                    }
                )
                Result.success()
            } catch (ex: Exception) {
                Log.e(FetchStudyWorker::javaClass.name, ex.stackTraceToString())
                collectAppLogUseCase(
                    AppLog(FetchStudyWorker::class.simpleName).also {
                        it.put("isSuccess", "false")
                        it.put(
                            "message", "Failed to to fetch studies: ${ex.stackTraceToString()}"
                        )
                    }
                )
                Result.retry()
            }
        }
    }
}
