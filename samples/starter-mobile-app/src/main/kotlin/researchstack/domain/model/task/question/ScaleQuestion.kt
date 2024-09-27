package researchstack.domain.model.task.question

import researchstack.domain.model.task.question.common.Question
import researchstack.domain.model.task.question.common.QuestionTag
import researchstack.domain.model.task.question.common.QuestionType

@Suppress("LongParameterList")
class ScaleQuestion(
    id: String,
    title: String,
    explanation: String,
    tag: QuestionTag,
    isRequired: Boolean,
    val low: Int,
    val high: Int,
    val lowLabel: String,
    val highLabel: String,
) : Question(id, title, explanation, tag, isRequired, QuestionType.SCALE) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ScaleQuestion) return false
        if (!super.equals(other)) return false

        if (low != other.low) return false
        if (high != other.high) return false
        if (lowLabel != other.lowLabel) return false
        if (highLabel != other.highLabel) return false

        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + low
        result = 31 * result + high
        result = 31 * result + lowLabel.hashCode()
        result = 31 * result + highLabel.hashCode()
        return result
    }
}
