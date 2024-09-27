package researchstack.domain.model.eligibilitytest.answer

import researchstack.domain.model.task.question.common.QuestionType

class ScaleAnswer(
    questionId: String,
    val from: Int,
    val to: Int,
) : Answer(questionId, QuestionType.SCALE) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ScaleAnswer) return false
        if (!super.equals(other)) return false

        if (from != other.from) return false
        if (to != other.to) return false

        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + from
        result = 31 * result + to
        return result
    }
}
