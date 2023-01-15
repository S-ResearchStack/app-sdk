package healthstack.kit.task.onboarding.model

import healthstack.kit.task.base.StepModel

open class EligibilityIntroModel(
    id: String,
    title: String,
    val description: String,
    drawableId: Int? = null,
    val conditions: List<EligibilityCondition>,
    val viewType: ViewType,
) : StepModel(id, title, drawableId) {

    data class EligibilityCondition(
        val title: String,
        val constraints: List<String>,
    )

    enum class ViewType {
        Card,
        Paragraph,
    }
}
