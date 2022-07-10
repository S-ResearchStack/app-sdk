package com.samsung.healthcare.kit.repository

import com.samsung.healthcare.kit.model.question.ChoiceQuestionModel
import com.samsung.healthcare.kit.task.SurveyTask
import com.samsung.healthcare.kit.task.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.util.UUID

// TODO this file will be deleted. this is ust fake for temporal use.
class RoomTaskRepository : TaskRepository {
    private val upcomingTasks =
        mutableListOf<Task>(
            sampleSurveyTask(),
            sampleSurveyTask(),
            sampleSurveyTask(),
            sampleSurveyTask(),
        )

    private val completedTasks =
        mutableListOf<Task>()

    override fun done(task: Task) {
        upcomingTasks.remove(task)
        completedTasks.add(task)
    }

    override fun getUpcomingDailyTasks(date: LocalDate): Flow<List<Task>> {
        return flow {
            emit(upcomingTasks.toList())
        }
    }

    override fun getCompletedDailyTasks(date: LocalDate): Flow<List<Task>> {
        return flow {
            emit(completedTasks.toList())
        }
    }

    fun sampleSurveyTask(): SurveyTask =
        SurveyTask.Builder(
            UUID.randomUUID().toString(),
            "Good Survey",
            "good description",
            { }
        ).apply {
            addQuestion(
                ChoiceQuestionModel(
                    "choice-question-model-1",
                    "Do you have any existing cardiac conditions?",
                    "Examples of cardiac conditions include abnormal heart rhythms, or arrhythmias",
                    candidates = listOf("Yes", "No"),
                    answer = "Yes"
                ),
            )
            addQuestion(
                ChoiceQuestionModel(
                    "choice-question-model-2",
                    "Do you currently own a wearable device?",
                    "Examples of wearable devices include Samsung Galaxy Watch 4, Fitbit, OuraRing, etc.",
                    candidates = listOf("Yes", "No"),
                    answer = "Yes"
                ),
            )
            addQuestion(
                ChoiceQuestionModel(
                    "choice-question-model-3",
                    "test page?",
                    candidates = listOf("Yes", "No"),
                    answer = "Yes"
                ),
            )
        }.build()
}
