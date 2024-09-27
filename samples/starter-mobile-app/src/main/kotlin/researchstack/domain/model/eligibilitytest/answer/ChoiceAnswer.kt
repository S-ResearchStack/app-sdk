package researchstack.domain.model.eligibilitytest.answer

import researchstack.domain.model.task.question.common.Option
import researchstack.domain.model.task.question.common.QuestionType

class ChoiceAnswer(
    questionId: String,
    val options: List<Option>,
) : Answer(questionId, QuestionType.CHOICE) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChoiceAnswer

        if (options != other.options) return false

        return super.equals(other)
    }

    override fun hashCode(): Int {
        return options.hashCode()
    }
}
