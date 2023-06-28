package healthstack.kit.task.survey.question.model

import healthstack.kit.task.survey.question.model.MultiChoiceQuestionModel.ViewType.Checkbox
import healthstack.kit.task.survey.question.model.QuestionModel.QuestionType.MultipleChoice

open class MultiChoiceQuestionModel(
    id: String,
    query: String,
    explanation: String? = null,
    drawableId: Int? = null,
    answer: String? = null,
    skipLogics: List<SkipLogic> = emptyList(),
    val candidates: List<String>, // image source
    tag: String,
) : QuestionModel<String>(id, query, explanation, drawableId, MultipleChoice, skipLogics, answer) {

    private val selections: MutableSet<Int> = mutableSetOf()

    val viewType: ViewType =
        if (tag.uppercase() == Checkbox.title)
            Checkbox
        else
            throw IllegalArgumentException("not supported")

    init {
        candidates.ifEmpty { throw IllegalArgumentException("at least one candidate is required.") }
    }

    fun select(index: Int) {
        require(0 <= index && index < candidates.size)
        selections.add(index)
    }

    fun deselect(index: Int) {
        require(0 <= index && index < candidates.size)
        selections.remove(index)
    }

    fun isSelected(index: Int) = selections.contains(index)

    enum class ViewType(val title: String) {
        Checkbox("CHECKBOX"),
    }

    override fun getResponse(): String =
        selections.sorted().joinToString(",") { candidates[it] }
}
