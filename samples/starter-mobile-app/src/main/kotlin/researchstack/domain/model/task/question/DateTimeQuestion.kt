package researchstack.domain.model.task.question

import researchstack.domain.model.task.question.common.Question
import researchstack.domain.model.task.question.common.QuestionTag
import researchstack.domain.model.task.question.common.QuestionType

@Suppress("LongParameterList")
class DateTimeQuestion(
    id: String,
    title: String,
    explanation: String,
    tag: QuestionTag,
    isRequired: Boolean,
    val isDate: Boolean,
    val isTime: Boolean,
    val isRange: Boolean,
) : Question(id, title, explanation, tag, isRequired, QuestionType.DATETIME) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DateTimeQuestion) return false
        if (!super.equals(other)) return false

        if (isDate != other.isDate) return false
        if (isTime != other.isTime) return false
        if (isRange != other.isRange) return false

        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + isDate.hashCode()
        result = 31 * result + isTime.hashCode()
        result = 31 * result + isRange.hashCode()
        return result
    }
}
