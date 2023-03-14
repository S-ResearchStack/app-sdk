package healthstack.backend.integration.task

/**
 * Data Transfer Object for uploading the result of the task.
 *
 * @property userId ID of the user who upload the task result.
 * @property taskId ID of the completed task.
 * @property revisionId If the task is modified, the modified ID for history management.
 * @property startedAt Time when the user started the task.
 * @property submittedAt Time when the user submitted the task.
 * @property itemResults Results of items belonging to task.
 */
data class TaskResult(
    val userId: String,
    val taskId: String,
    val revisionId: Int,
    val startedAt: String,
    val submittedAt: String,
    val itemResults: List<ItemResult>,
)
