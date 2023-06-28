package healthstack.kit.task.survey.question.model

import healthstack.kit.task.survey.question.model.ImageChoiceQuestionModel.ViewType.ImageWithLabel
import healthstack.kit.task.survey.question.model.QuestionModel.QuestionType.Image

class ImageChoiceQuestionModel(
    id: String,
    query: String,
    explanation: String? = null,
    drawableId: Int? = null,
    answer: String? = null,
    skipLogics: List<SkipLogic> = emptyList(),
    val candidates: List<String>, // image source
    val labels: List<String>? = null, // image label
    tag: String,
) : QuestionModel<String>(id, query, explanation, drawableId, Image, skipLogics, answer) {

    private val selections: MutableSet<Int> = mutableSetOf()
    var selection: Int? = null

    val isMulti: Boolean =
        tag.uppercase() == "MULTIIMAGE"

    val viewType: ViewType =
        if (labels.isNullOrEmpty()) ViewType.Image else ImageWithLabel

    init {
        candidates.ifEmpty { throw IllegalArgumentException("at least one candidate is required.") }
        if (!labels.isNullOrEmpty() && candidates.size != labels.size)
            throw IllegalArgumentException("the size of candidate and label should be the same.")
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
        Image("IMAGE"),
        ImageWithLabel("IMAGEWITHLABEL")
    }

    override fun getResponse(): String =
        if (isMulti) selections.sorted().joinToString(",") { candidates[it] }
        else selection?.let { candidates[it] } ?: ""
}
