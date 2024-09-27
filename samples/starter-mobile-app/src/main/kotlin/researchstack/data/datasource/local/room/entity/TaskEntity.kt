package researchstack.data.datasource.local.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import researchstack.domain.model.task.Task
import researchstack.domain.model.task.taskresult.TaskResult
import java.time.LocalDateTime

@Entity(
    tableName = "task",
    indices = [
        Index(value = ["taskId", "scheduledAt"], unique = true),
        Index(value = ["finishedDate"])
    ],
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val taskId: String,
    val scheduledAt: LocalDateTime,
    val validUntil: LocalDateTime,
    var taskResult: TaskResult? = null,
    var finishedDate: LocalDateTime? = null,
    var studyId: String,
    val task: Task,
)
