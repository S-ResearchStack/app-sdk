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
import researchstack.domain.usecase.task.FetchStudyTasksUseCase
import researchstack.presentation.exception.NoStudyId

@HiltWorker
class FetchStudyTasksWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val fetchStudyTasksUseCase: FetchStudyTasksUseCase,
    private val collectAppLogUseCase: CollectAppLogUseCase
) : CoroutineWorker(appContext, workerParams) {

    @Suppress("TooGenericExceptionCaught")
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val studyId = inputData.getString(STUDY_ID_KEY)
            try {
                studyId?.let {
                    fetchStudyTasksUseCase(studyId)
                } ?: throw NoStudyId
                collectAppLogUseCase(
                    AppLog(FetchStudyTasksWorker::class.simpleName).also {
                        it.put("isSuccess", "true")
                        it.put("message", "Succeeded to fetch tasks with study id: $studyId")
                    }
                )
                Result.success()
            } catch (ex: Exception) {
                Log.e(FetchStudyTasksWorker::class.simpleName, ex.stackTraceToString())
                collectAppLogUseCase(
                    AppLog(FetchStudyTasksWorker::class.simpleName).also {
                        it.put("isSuccess", "false")
                        it.put(
                            "message",
                            "Failed to fetch task with study id: $studyId: ${ex.stackTraceToString()}"
                        )
                    }
                )
                Result.retry()
            }
        }
    }

    companion object {
        const val STUDY_ID_KEY = "studyId"
    }
}
