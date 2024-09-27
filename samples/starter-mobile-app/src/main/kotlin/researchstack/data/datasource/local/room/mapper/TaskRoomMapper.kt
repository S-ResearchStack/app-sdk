package researchstack.data.datasource.local.room.mapper

import researchstack.data.datasource.local.room.entity.TaskEntity

fun TaskEntity.toDomain() = task.apply {
    id = this@toDomain.id
    taskResult = this@toDomain.taskResult
}
