package healthstack.backend.integration.task

import java.time.LocalDateTime

/**
 * Interface for get task from the backend and upload result to the backend.
 */
interface TaskClient {
    /**
     * Retrieves the task that has been published since the lastSyncTime the task was retrieved.
     *
     * @param idToken An encrypted token containing the user's information issued when the logs in to the application.
     * @param lastSyncTime Get tasks that published after lastSyncTime
     * @param endTime Retrieves all tasks created before endTime
     * @return
     */
    suspend fun getTasks(
        idToken: String,
        lastSyncTime: LocalDateTime,
        endTime: LocalDateTime = LocalDateTime.now(),
    ): List<TaskSpec>

    /**
     * Uploads the user's task execution result to Backend.
     *
     * @param idToken An encrypted token containing the user's information issued when the logs in to the application.
     * @param result [healthstack.backend.integration.task.TaskResult]
     */
    suspend fun uploadTaskResult(idToken: String, result: TaskResult)
}
