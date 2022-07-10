package com.samsung.healthcare.kit.model.question

class MultiChoiceQuestionModel(
    id: String,
    query: String,
    explanation: String? = null,
    drawableId: Int? = null,
    answer: String? = null,
    val candidates: List<String>,
) : QuestionModel<String>(id, query, explanation, drawableId, QuestionType.MultipleChoice, answer) {

    private val selections: MutableSet<Int> = mutableSetOf()

    init {
        candidates.ifEmpty { throw IllegalArgumentException("at least one candidate is required.") }
    }

    fun select(index: Int) {
        require(0 <= index && index < candidates.size)
        selections.add(index)
    }

    fun isSelected(index: Int) = selections.contains(index)

    override fun getResponse(): String = selections.sorted().map { candidates[it] }.joinToString(", ")
}
