package researchstack.data.repository

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import researchstack.data.datasource.grpc.GrpcTaskDataSource
import researchstack.data.datasource.local.room.dao.TaskDao
import researchstack.data.datasource.local.room.entity.TaskEntity
import researchstack.data.datasource.local.room.mapper.toDomain
import researchstack.data.repository.TaskRepositoryImpl.UploadWorker.Companion.SCHEDULED_TASK_ID_KEY
import researchstack.domain.model.task.Task
import researchstack.domain.model.task.taskresult.TaskResult
import researchstack.domain.repository.TaskRepository
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@Suppress("TooManyFunctions")
class TaskRepositoryImpl @Inject constructor(
    private val workerManager: WorkManager,
    private val taskDao: TaskDao,
    private val grpcTaskDataSource: GrpcTaskDataSource,
) : TaskRepository {
    override fun getTodayTasks(targetTime: LocalDateTime): Flow<List<Task>> =
        taskDao.getTodayTasks(targetTime.toString()).flowOn(Dispatchers.IO)
            .map { it.map { entity -> entity.toDomain() } }

    override fun getActiveTasks(targetTime: LocalDateTime): Flow<List<Task>> =
        taskDao.getActiveTasks(targetTime.toString()).flowOn(Dispatchers.IO)
            .map { it.map { entity -> entity.toDomain() } }

    override fun getUpcomingTasks(targetTime: LocalDateTime): Flow<List<Task>> =
        taskDao.getUpcomingTasks(
            targetTime.toString(),
            targetTime.toLocalDate().plusDays(1).toString()
        ).flowOn(Dispatchers.IO).map { it.map { entity -> entity.toDomain() } }

    override fun getCompletedTasks(targetDay: LocalDate): Flow<List<Task>> =
        taskDao.getCompletedTasks(targetDay.atStartOfDay().toString())
            .flowOn(Dispatchers.IO)
            .map { it.map { entity -> entity.toDomain() } }

    override suspend fun removeAllTasks(): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching { taskDao.removeAll() }
    }.onFailure {
        Log.d(TAG, it.stackTraceToString())
        Log.e(TAG, it.message ?: "")
    }

    override suspend fun removeStudyTasks(studyId: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching { taskDao.removeByStudyId(studyId) }
    }.onFailure {
        Log.d(TAG, it.stackTraceToString())
        Log.e(TAG, it.message ?: "")
    }

    override suspend fun saveResult(taskResult: TaskResult): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            taskDao.setResult(taskResult.id, taskResult, LocalDate.now().atStartOfDay().toString())
        }.onFailure {
            Log.d(TAG, it.stackTraceToString())
            Log.e(TAG, it.message ?: "")
        }
    }

    override suspend fun fetchNewTasks(lastSyncTime: LocalDateTime): Result<Unit> =
        saveTask { grpcTaskDataSource.getAllNewTask(lastSyncTime) }

    override suspend fun fetchTasksOfStudy(studyId: String): Result<Unit> =
        saveTask { grpcTaskDataSource.getTaskOfStudy(studyId) }

    override suspend fun fetchMyTasks(): Result<Unit> =
        saveTask { grpcTaskDataSource.getAllMyTaskSpec() }

    private suspend fun saveTask(getTaskSpecFunc: suspend () -> Result<List<Flow<TaskEntity>>>) =
        withContext(Dispatchers.IO) {
            runCatching {
                getTaskSpecFunc().getOrThrow().forEach {
                    it.collect { taskEntity -> taskDao.insertAll(listOf(taskEntity)) }
                }
            }.onFailure {
                Log.d(TAG, it.stackTraceToString())
                Log.e(TAG, it.message ?: "")
            }
        }

    override suspend fun uploadTaskResult(scheduledTaskId: Int): Result<Unit> =
        withContext(Dispatchers.IO) {
            runCatching {
                val inputData =
                    Data.Builder().putInt(SCHEDULED_TASK_ID_KEY, scheduledTaskId).build()
                val uploadWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<UploadWorker>()
                    .setInputData(inputData)
                    .build()
                workerManager.enqueue(uploadWorkRequest)
                return@runCatching
            }.onFailure {
                Log.d(TAG, it.stackTraceToString())
                Log.e(TAG, it.message ?: "")
            }
        }

    @HiltWorker
    class UploadWorker @AssistedInject constructor(
        @Assisted appContext: Context,
        @Assisted workerParams: WorkerParameters,
        private val taskDao: TaskDao,
        private val grpcTaskDataSource: GrpcTaskDataSource,
    ) : CoroutineWorker(appContext, workerParams) {
        override suspend fun doWork(): Result {
            val scheduledTaskId = inputData.getInt(SCHEDULED_TASK_ID_KEY, -1)
            if (scheduledTaskId == -1) {
                Log.e(TAG, "The scheduledTaskId is not set")
                return Result.failure()
            }
            return uploadTaskResult(scheduledTaskId)
        }

        private suspend fun uploadTaskResult(scheduledTaskId: Int): Result =
            runCatching {
                val task = taskDao.findById(scheduledTaskId)
                    ?: throw IllegalArgumentException("A task with id($scheduledTaskId) does not exist")

                val result = task.toDomain().taskResult
                    ?: throw IllegalArgumentException("There is no saved result for this task")

                grpcTaskDataSource.uploadResults(task.task.studyId, task.taskId, result)
                Result.success()
            }.getOrElse { throwable ->
                Log.d(TAG, throwable.stackTraceToString())
                Log.e(TAG, throwable.message ?: "")
                if (throwable is IllegalArgumentException) Result.failure()
                else Result.retry()
            }

        companion object {
            const val SCHEDULED_TASK_ID_KEY: String = "scheduled_task_id"
        }
    }

    companion object {
        private val TAG = TaskRepositoryImpl::class.simpleName
    }
}
