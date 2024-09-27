package researchstack.domain.model.task.taskresult

import researchstack.domain.model.task.TaskType
import java.time.LocalDateTime

sealed class TaskResult(
    val id: Int,
    val startedAt: LocalDateTime,
    val finishedAt: LocalDateTime,
    val type: TaskType,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TaskResult) return false

        if (id != other.id) return false
        if (startedAt != other.startedAt) return false
        if (finishedAt != other.finishedAt) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + startedAt.hashCode()
        result = 31 * result + finishedAt.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}
