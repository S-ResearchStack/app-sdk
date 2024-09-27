package researchstack.domain.model.task.taskresult

import researchstack.domain.model.task.TaskType
import researchstack.domain.model.task.question.common.QuestionResult
import java.time.LocalDateTime

class SurveyResult(
    id: Int,
    startedAt: LocalDateTime,
    finishedAt: LocalDateTime,
    val questionResults: List<QuestionResult>
) : TaskResult(id, startedAt, finishedAt, TaskType.SURVEY)
