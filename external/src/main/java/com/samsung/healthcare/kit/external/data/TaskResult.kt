package com.samsung.healthcare.kit.external.data

data class TaskResult(
    val userId: String,
    val taskId: String,
    val revisionId: Int,
    val startedAt: String,
    val submittedAt: String,
    val itemResults: List<ItemResult>
)
