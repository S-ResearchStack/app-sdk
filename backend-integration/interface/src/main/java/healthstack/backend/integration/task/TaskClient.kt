package healthstack.backend.integration.task

import java.time.LocalDateTime

interface TaskClient {
    suspend fun getTasks(
        idToken: String,
        lastSyncTime: LocalDateTime,
        endTime: LocalDateTime = LocalDateTime.now(),
    ): List<TaskSpec>

    suspend fun uploadTaskResult(idToken: String, result: TaskResult)
}
