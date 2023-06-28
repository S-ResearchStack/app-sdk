package healthstack.kit.task.survey.question.model

import healthstack.kit.task.survey.question.model.DateTimeQuestionModel.ViewType.Date
import healthstack.kit.task.survey.question.model.DateTimeQuestionModel.ViewType.DateRange
import healthstack.kit.task.survey.question.model.DateTimeQuestionModel.ViewType.DateTime
import healthstack.kit.task.survey.question.model.DateTimeQuestionModel.ViewType.DateTimeRange
import healthstack.kit.task.survey.question.model.DateTimeQuestionModel.ViewType.Time
import healthstack.kit.task.survey.question.model.DateTimeQuestionModel.ViewType.TimeRange
import healthstack.kit.task.survey.question.model.QuestionModel.QuestionType.DateTime as QuestionDateTime

class DateTimeQuestionModel(
    id: String,
    query: String,
    explanation: String? = null,
    drawableId: Int? = null,
    skipLogics: List<SkipLogic> = emptyList(),
    answer: String? = null,
    isTime: Boolean,
    isDate: Boolean,
    isRange: Boolean,
) : QuestionModel<String>(id, query, explanation, drawableId, QuestionDateTime, skipLogics, answer) {
    val viewType: ViewType =
        if (isRange) {
            if (isTime && isDate) DateTimeRange
            else if (isTime) TimeRange
            else if (isDate) DateRange
            else throw IllegalArgumentException("not supported view type")
        } else {
            if (isTime && isDate) DateTime
            else if (isTime) Time
            else if (isDate) Date
            else throw IllegalArgumentException("not supported view type")
        }

    var result: String = ""

    override fun getResponse(): String = result

    enum class ViewType {
        Date,
        Time,
        DateTime,
        TimeRange,
        DateRange,
        DateTimeRange,
    }
}
