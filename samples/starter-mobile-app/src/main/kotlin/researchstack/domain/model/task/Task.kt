package researchstack.domain.model.task

import researchstack.domain.model.task.taskresult.TaskResult
import java.time.LocalDateTime

@Suppress("LongParameterList")
sealed class Task(
    var id: Int? = null,
    val taskId: String,
    val studyId: String,
    val title: String,
    val description: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val scheduledAt: LocalDateTime,
    val validUntil: LocalDateTime,
    val inClinic: Boolean,
    var taskResult: TaskResult? = null,
    private val type: TaskType,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Task) return false

        if (id != other.id) return false
        if (taskId != other.taskId) return false
        if (studyId != other.studyId) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (createdAt != other.createdAt) return false
        if (scheduledAt != other.scheduledAt) return false
        if (validUntil != other.validUntil) return false
        if (inClinic != other.inClinic) return false
        if (taskResult != other.taskResult) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + taskId.hashCode()
        result = 31 * result + studyId.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + scheduledAt.hashCode()
        result = 31 * result + validUntil.hashCode()
        result = 31 * result + inClinic.hashCode()
        result = 31 * result + (taskResult?.hashCode() ?: 0)
        result = 31 * result + type.hashCode()
        return result
    }
}
