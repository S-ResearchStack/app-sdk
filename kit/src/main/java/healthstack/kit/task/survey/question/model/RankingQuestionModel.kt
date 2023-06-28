package healthstack.kit.task.survey.question.model

import healthstack.kit.task.survey.question.model.QuestionModel.QuestionType.Ranking

class RankingQuestionModel(
    id: String,
    query: String,
    explanation: String? = null,
    drawableId: Int? = null,
    answer: String? = null,
    skipLogics: List<SkipLogic> = emptyList(),
    val candidates: List<String>,
) : QuestionModel<String>(id, query, explanation, drawableId, Ranking, skipLogics, answer) {
    var selection: String = candidates.toString()

    init {
        candidates.ifEmpty { throw IllegalArgumentException("at least one candidate is required.") }
    }

    override fun getResponse(): String =
        selection
}
