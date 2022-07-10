package com.samsung.healthcare.kit.repository

import com.google.firebase.auth.FirebaseAuth
import com.samsung.healthcare.kit.entity.Task
import com.samsung.healthcare.kit.external.data.ItemResult
import com.samsung.healthcare.kit.external.data.TaskResult
import com.samsung.healthcare.kit.external.network.ResearchPlatformAdapter
import com.samsung.healthcare.kit.task.SurveyTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import com.samsung.healthcare.kit.task.Task as ViewTask

class TaskRepositoryImpl : TaskRepository {
    private val taskDao = TaskRoomDatabase.getInstance().taskDao()

    override suspend fun updateResult(task: ViewTask) {
        if (task is SurveyTask) taskDao.setResult(
            task.id,
            task.step.subStepHolder.getResult(),
            task.startedAt.toString(),
            LocalDateTime.now().toString()
        )
        else
            taskDao.setSubmittedAt(task.id, LocalDateTime.now().toString())

        uploadTaskResult(task.id)
    }

    private suspend fun uploadTaskResult(id: String) {
        taskDao.findById(id).collect { task ->
            FirebaseAuth.getInstance().currentUser?.let { user ->
                user.getIdToken(false).addOnSuccessListener { tokenResult ->
                    tokenResult.token?.let { token ->
                        CoroutineScope(Dispatchers.IO).launch {
                            ResearchPlatformAdapter.getInstance().uploadResult(
                                token,
                                TaskResult(
                                    userId = user.uid,
                                    taskId = task.taskId,
                                    revisionId = task.revisionId,
                                    startedAt = task.startedAt.toString(),
                                    submittedAt = task.submittedAt.toString(),
                                    itemResults = task.result?.map {
                                        ItemResult(
                                            itemName = it.questionId,
                                            result = it.response
                                        )
                                    } ?: emptyList()
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    override suspend fun insertAll(taskEntities: List<Task>) {
        taskDao.insertAll(taskEntities)
    }

    override fun getActiveDailyTask(targetTime: LocalDateTime): Flow<List<ViewTask>> =
        taskDao.getActiveTasks(targetTime.toString())
            .map { tasks ->
                tasks.map { it.toViewTask() }
            }

    override fun getUpcomingDailyTask(targetTime: LocalDateTime): Flow<List<ViewTask>> =
        taskDao.getUpcomingTasks(
            targetTime.toString(),
            targetTime.toLocalDate().plusDays(1).toString()
        ).map { tasks ->
            tasks.map { it.toViewTask() }
        }

    override fun getCompletedDailyTasks(now: LocalDate): Flow<List<ViewTask>> {
        return taskDao.getCompletedTasks(
            now.toString(),
            now.plusDays(1).toString()
        ).map { tasks ->
            tasks.map { it.toViewTask() }
        }
    }
}
