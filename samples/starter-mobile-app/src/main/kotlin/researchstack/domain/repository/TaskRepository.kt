package researchstack.domain.repository

import kotlinx.coroutines.flow.Flow
import researchstack.domain.model.task.Task
import researchstack.domain.model.task.taskresult.TaskResult
import java.time.LocalDate
import java.time.LocalDateTime

interface TaskRepository {
    fun getTodayTasks(targetTime: LocalDateTime): Flow<List<Task>>

    fun getActiveTasks(targetTime: LocalDateTime): Flow<List<Task>>

    fun getUpcomingTasks(targetTime: LocalDateTime): Flow<List<Task>>

    fun getCompletedTasks(targetDay: LocalDate): Flow<List<Task>>

    suspend fun removeAllTasks(): Result<Unit>

    suspend fun removeStudyTasks(studyId: String): Result<Unit>

    suspend fun saveResult(taskResult: TaskResult): Result<Unit>

    suspend fun fetchNewTasks(lastSyncTime: LocalDateTime): Result<Unit>

    suspend fun uploadTaskResult(scheduledTaskId: Int): Result<Unit>

    suspend fun fetchTasksOfStudy(studyId: String): Result<Unit>

    suspend fun fetchMyTasks(): Result<Unit>
}
