package com.samsung.healthcare.kit.model.question

import com.samsung.healthcare.kit.model.question.ChoiceQuestionModel.ViewType.Slider

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
                if (value < 0)
                    throw IndexOutOfBoundsException("selected value is out of bound.")
            }

            field = value
        }

    init {
        candidates.ifEmpty { throw IllegalArgumentException("at least one candidate is required.") }
    }

    override fun getResponse(): R? =
        if (viewType != Slider)
            selection?.let { candidates[it] }
        else
            selection as R

    enum class ViewType {
        Slider,
        Radio,
        DropMenu
    }
}
