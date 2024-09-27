package researchstack.domain.model.eligibilitytest.answer

import researchstack.domain.model.task.question.common.QuestionType
import java.time.LocalDate
import java.time.LocalTime

class DateTimeAnswer(
    questionId: String,
    val fromDate: LocalDate?,
    val toDate: LocalDate?,
    val fromTime: LocalTime?,
    val toTime: LocalTime?,
) : Answer(questionId, QuestionType.DATETIME) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DateTimeAnswer) return false
        if (!super.equals(other)) return false

        if (fromDate != other.fromDate) return false
        if (toDate != other.toDate) return false
        if (fromTime != other.fromTime) return false
        if (toTime != other.toTime) return false

        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (fromDate?.hashCode() ?: 0)
        result = 31 * result + (toDate?.hashCode() ?: 0)
        result = 31 * result + (fromTime?.hashCode() ?: 0)
        result = 31 * result + (toTime?.hashCode() ?: 0)
        return result
    }
}
