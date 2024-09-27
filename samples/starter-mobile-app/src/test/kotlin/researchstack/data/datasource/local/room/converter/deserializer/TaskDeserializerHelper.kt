package researchstack.data.datasource.local.room.converter.deserializer

import com.google.gson.JsonDeserializer
import researchstack.domain.model.task.ActivityTask
import researchstack.domain.model.task.ActivityType
import researchstack.domain.model.task.Section
import researchstack.domain.model.task.SurveyTask
import researchstack.domain.model.task.Task
import researchstack.domain.model.task.question.common.Question
import researchstack.domain.model.task.question.common.QuestionTag
import researchstack.domain.model.task.question.common.QuestionType
import researchstack.domain.model.task.taskresult.ActivityResult
import researchstack.domain.model.task.taskresult.SurveyResult
import java.time.LocalDateTime

class TaskDeserializerHelper : JsonDeserializerHelper<Task>() {

    override var elements: Array<Task> = arrayOf(
        SurveyTask(
            0,
            "taskId",
            "studyId",
            "title",
            "description",
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            true,
            SurveyResult(0, LocalDateTime.now(), LocalDateTime.now(), listOf()),
            listOf(
                Section(
                    listOf(
                        Question(
                            "id",
                            "title",
                            "explanation",
                            QuestionTag.RANK,
                            true,
                            QuestionType.RANK
                        )
                    )
                )
            )
        ),

        ActivityTask(
            0,
            "taskId",
            "studyId",
            "title",
            "description",
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now(),
            true,
            ActivityResult(
                0,
                LocalDateTime.now(),
                LocalDateTime.now(),
                ActivityType.STROOP_TEST,
                mapOf("abc" to "abc")
            ),
            "completionTitle",
            "completionDescription",
            ActivityType.STROOP_TEST
        )
    )

    override var jsonDeserializer = TaskDeserializer() as JsonDeserializer<Task>
}
