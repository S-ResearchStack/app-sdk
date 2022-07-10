package com.samsung.healthcare.kit.repository

import com.samsung.healthcare.kit.task.Task
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TaskRepository {
    fun getUpcomingDailyTasks(date: LocalDate): Flow<List<Task>>

    fun getCompletedDailyTasks(date: LocalDate): Flow<List<Task>>

    fun done(task: Task)
}
