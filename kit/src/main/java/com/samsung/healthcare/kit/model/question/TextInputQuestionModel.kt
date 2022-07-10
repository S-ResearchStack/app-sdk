package com.samsung.healthcare.kit.model.question

class TextInputQuestionModel(
    id: String,
    query: String,
    explanation: String? = null,
    drawableId: Int? = null,
    answer: String? = null,
) : QuestionModel<String>(id, query, explanation, drawableId, QuestionType.Text, answer) {
    var input: String = ""

    override fun getResponse(): String = input
}
