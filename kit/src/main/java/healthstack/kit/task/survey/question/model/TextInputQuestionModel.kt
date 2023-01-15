package healthstack.kit.task.survey.question.model

import healthstack.kit.task.survey.question.model.QuestionModel.QuestionType.Text

class TextInputQuestionModel(
    id: String,
    query: String,
    explanation: String? = null,
    drawableId: Int? = null,
    answer: String? = null,
) : QuestionModel<String>(id, query, explanation, drawableId, Text, answer) {
    var input: String = ""

    override fun getResponse(): String = input
}
