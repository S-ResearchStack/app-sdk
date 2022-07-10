package com.samsung.healthcare.kit.external.network

import com.samsung.healthcare.kit.external.data.TaskResult
import com.samsung.healthcare.kit.external.data.TaskSpec
import java.time.LocalDateTime

interface TaskClient {
    suspend fun getTasks(idToken: String, lastSyncTime: LocalDateTime): List<TaskSpec>

    suspend fun uploadResult(idToken: String, result: TaskResult)
}
