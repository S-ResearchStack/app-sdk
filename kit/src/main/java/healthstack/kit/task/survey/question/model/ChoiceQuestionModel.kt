package healthstack.kit.task.survey.question.model

import healthstack.kit.task.survey.question.model.ChoiceQuestionModel.ViewType.Radio
import healthstack.kit.task.survey.question.model.ChoiceQuestionModel.ViewType.Slider
import healthstack.kit.task.survey.question.model.QuestionModel.QuestionType.Choice

class ChoiceQuestionModel<R>(
    id: String,
    query: String,
    explanation: String? = null,
    drawableId: Int? = null,
    answer: R? = null,
    skipLogics: List<SkipLogic> = emptyList(),

    val candidates: List<R>,
    val viewType: ViewType = Radio,
) : QuestionModel<R>(id, query, explanation, drawableId, Choice, skipLogics, answer) {

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
