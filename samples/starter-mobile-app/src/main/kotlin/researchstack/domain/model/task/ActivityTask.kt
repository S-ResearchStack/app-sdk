package researchstack.domain.model.task

import researchstack.domain.model.task.taskresult.ActivityResult
import java.time.LocalDateTime

@Suppress("LongParameterList")
class ActivityTask(
    id: Int? = null,
    taskId: String,
    studyId: String,
    title: String,
    description: String,
    createdAt: LocalDateTime = LocalDateTime.now(),
    scheduledAt: LocalDateTime,
    validUntil: LocalDateTime,
    inClinic: Boolean,
    activityResult: ActivityResult? = null,
    val completionTitle: String,
    val completionDescription: String,
    val activityType: ActivityType,
) : Task(
    id,
    taskId,
    studyId,
    title,
    description,
    createdAt,
    scheduledAt,
    validUntil,
    inClinic,
    activityResult,
    TaskType.ACTIVITY
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ActivityTask) return false
        if (!super.equals(other)) return false

        if (completionTitle != other.completionTitle) return false
        if (completionDescription != other.completionDescription) return false
        if (activityType != other.activityType) return false

        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + completionTitle.hashCode()
        result = 31 * result + completionDescription.hashCode()
        result = 31 * result + activityType.hashCode()
        return result
    }
}
