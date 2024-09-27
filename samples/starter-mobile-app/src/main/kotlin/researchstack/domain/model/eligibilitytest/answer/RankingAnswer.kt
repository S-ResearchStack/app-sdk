package researchstack.domain.model.eligibilitytest.answer

import researchstack.domain.model.task.question.common.QuestionType

class RankingAnswer(
    questionId: String,
    val answers: List<String>,
) : Answer(questionId, QuestionType.RANK) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RankingAnswer) return false
        if (!super.equals(other)) return false

        if (answers != other.answers) return false

        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + answers.hashCode()
        return result
    }
}
