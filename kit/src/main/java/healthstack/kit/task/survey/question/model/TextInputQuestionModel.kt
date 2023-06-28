package healthstack.kit.task.survey.question.model

import healthstack.kit.task.survey.question.model.QuestionModel.QuestionType.Text

class TextInputQuestionModel(
    id: String,
    query: String,
    explanation: String? = null,
    drawableId: Int? = null,
    skipLogics: List<SkipLogic> = emptyList(),
    answer: String? = null,
    val maxCharacter: Int = 500,
) : QuestionModel<String>(id, query, explanation, drawableId, Text, skipLogics, answer) {
    var input: String = ""

    override fun getResponse(): String = input
}
