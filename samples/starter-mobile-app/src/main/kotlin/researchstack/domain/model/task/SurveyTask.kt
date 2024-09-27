package researchstack.domain.model.task

import researchstack.domain.model.task.taskresult.SurveyResult
import java.time.LocalDateTime

@Suppress("LongParameterList")
class SurveyTask(
    id: Int? = null,
    taskId: String,
    studyId: String,
    title: String,
    description: String,
    createdAt: LocalDateTime = LocalDateTime.now(),
    scheduledAt: LocalDateTime,
    validUntil: LocalDateTime,
    inClinic: Boolean,
    surveyResult: SurveyResult? = null,
    val sections: List<Section>,
) : Task(
    id,
    taskId,
    studyId,
    title,
    description,
    createdAt,
    scheduledAt,
    validUntil,
    inClinic,
    surveyResult,
    TaskType.SURVEY
)
