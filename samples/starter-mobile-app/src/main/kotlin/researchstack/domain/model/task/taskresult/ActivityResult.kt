package researchstack.domain.model.task.taskresult

import researchstack.domain.model.task.ActivityType
import researchstack.domain.model.task.TaskType
import java.time.LocalDateTime

class ActivityResult(
    id: Int,
    startedAt: LocalDateTime,
    finishedAt: LocalDateTime,
    val activityType: ActivityType,
    val result: Map<String, Any>,
) : TaskResult(id, startedAt, finishedAt, TaskType.ACTIVITY) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ActivityResult) return false

        if (result != other.result) return false

        return super.equals(other)
    }

    override fun hashCode(): Int {
        return result.hashCode()
    }
}
