package researchstack.domain.model.eligibilitytest.answer

import researchstack.domain.model.task.question.common.QuestionType

open class Answer(
    val questionId: String,
    val type: QuestionType,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Answer) return false

        if (questionId != other.questionId) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = questionId.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}
