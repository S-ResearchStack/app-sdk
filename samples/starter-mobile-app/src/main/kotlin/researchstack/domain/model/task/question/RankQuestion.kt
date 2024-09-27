package researchstack.domain.model.task.question

import researchstack.domain.model.task.question.common.Option
import researchstack.domain.model.task.question.common.Question
import researchstack.domain.model.task.question.common.QuestionTag
import researchstack.domain.model.task.question.common.QuestionType

class RankQuestion(
    id: String,
    title: String,
    explanation: String,
    tag: QuestionTag,
    isRequired: Boolean,
    val options: List<Option>,
) : Question(id, title, explanation, tag, isRequired, QuestionType.RANK) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RankQuestion) return false
        if (!super.equals(other)) return false

        if (options != other.options) return false

        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + options.hashCode()
        return result
    }
}
