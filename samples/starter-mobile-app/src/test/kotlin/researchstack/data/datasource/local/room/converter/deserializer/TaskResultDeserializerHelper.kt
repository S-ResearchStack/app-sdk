package researchstack.data.datasource.local.room.converter.deserializer

import com.google.gson.JsonDeserializer
import researchstack.domain.model.task.ActivityType
import researchstack.domain.model.task.question.common.QuestionResult
import researchstack.domain.model.task.taskresult.ActivityResult
import researchstack.domain.model.task.taskresult.SurveyResult
import researchstack.domain.model.task.taskresult.TaskResult
import java.time.LocalDateTime

class TaskResultDeserializerHelper : JsonDeserializerHelper<TaskResult>() {

    override var elements: Array<TaskResult> = arrayOf(
        ActivityResult(
            0,
            LocalDateTime.now(),
            LocalDateTime.now(),
            ActivityType.STROOP_TEST,
            mapOf("abc" to Unit)
        ),

        SurveyResult(
            0,
            LocalDateTime.now(),
            LocalDateTime.now(),
            listOf(QuestionResult("id", "result"))
        )
    )

    override var jsonDeserializer = TaskResultDeserializer() as JsonDeserializer<TaskResult>
}
