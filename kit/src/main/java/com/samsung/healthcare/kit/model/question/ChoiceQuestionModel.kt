package com.samsung.healthcare.kit.model.question

class ChoiceQuestionModel<R>(
    id: String,
    query: String,
    explanation: String? = null,
    drawableId: Int? = null,
    answer: R? = null,

    val candidates: List<R>,
    val viewType: ViewType = ViewType.Radio,
) : QuestionModel<R>(id, query, explanation, drawableId, QuestionType.Choice, answer) {

    var selection: Int? = null
        set(value) {
            if (value != null) {
                if (value < 0 || candidates.size <= value)
                    throw IndexOutOfBoundsException("selected value is out of bound.")
            }

            field = value
        }

    init {
        candidates.ifEmpty { throw IllegalArgumentException("at least one candidate is required.") }
    }

    override fun getResponse(): R? = selection?.let { candidates[it] }

    enum class ViewType {
        Slider,
        Radio,
        DropMenu
    }
}
