package healthstack.app.task.repository

import com.google.firebase.auth.FirebaseAuth
import healthstack.app.task.db.TaskRoomDatabase
import healthstack.app.task.entity.Task.Result
import healthstack.backend.integration.BackendFacadeHolder
import healthstack.backend.integration.task.ItemResult
import healthstack.backend.integration.task.TaskResult
import healthstack.kit.task.survey.SurveyTask
import healthstack.kit.task.survey.question.SubStepHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import healthstack.kit.task.base.Task as ViewTask

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
                            BackendFacadeHolder.getInstance().uploadTaskResult(
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

    override suspend fun insertAll(taskEntities: List<healthstack.app.task.entity.Task>) {
        taskDao.insertAll(taskEntities)
    }

    override fun getActiveTasks(targetTime: LocalDateTime): Flow<List<ViewTask>> =
        taskDao.getActiveTasks(targetTime.toString())
            .map { tasks ->
                tasks.map { it.toViewTask() }
            }

    override fun getUpcomingTasks(targetTime: LocalDateTime): Flow<List<ViewTask>> =
        taskDao.getUpcomingTasks(
            targetTime.toString(),
            targetTime.toLocalDate().plusDays(1).toString()
        ).map { tasks ->
            tasks.map { it.toViewTask() }
        }

    override fun getCompletedTasks(now: LocalDate): Flow<List<ViewTask>> {
        return taskDao.getCompletedTasks(
            now.toString(),
            now.plusDays(1).toString()
        ).map { tasks ->
            tasks.map { it.toViewTask() }
        }
    }
}

fun SubStepHolder.getResult(): List<Result> =
    subSteps.map {
        Result(
            it.model.id,
            it.getResult().toString()
        )
    }
