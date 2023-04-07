package healthstack.kit.task.survey.question.model

abstract class QuestionModel<R>(
    val id: String,
    val question: String,
    val explanation: String? = null,
    val drawableId: Int? = null,
    val type: QuestionType,
    val skipLogics: List<SkipLogic>,
    private val answer: R? = null,
) {

    fun isCorrect(): Boolean = if (null != answer) answer == getResponse() else true

    abstract fun getResponse(): R?

    enum class QuestionType {
        Choice,
        MultipleChoice,
        Text
    }
}

class SkipLogic(
    val condition: String,
    val goToItemSequence: Int
)
