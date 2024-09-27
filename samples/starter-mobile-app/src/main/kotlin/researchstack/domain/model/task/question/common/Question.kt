package researchstack.domain.model.task.question.common

open class Question(
    val id: String,
    val title: String,
    val explanation: String,
    val tag: QuestionTag,
    val isRequired: Boolean,
    private val type: QuestionType,
) {
    init {
        if (!type.tags.contains(tag)) throw IllegalArgumentException("Tag and question type don't match")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Question) return false

        if (id != other.id) return false
        if (title != other.title) return false
        if (explanation != other.explanation) return false
        if (tag != other.tag) return false
        if (isRequired != other.isRequired) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + explanation.hashCode()
        result = 31 * result + tag.hashCode()
        result = 31 * result + isRequired.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}
