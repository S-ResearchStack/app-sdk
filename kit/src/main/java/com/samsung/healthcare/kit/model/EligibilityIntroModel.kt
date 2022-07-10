package com.samsung.healthcare.kit.model

class EligibilityIntroModel(
    id: String,
    title: String,
    val description: String,
    drawableId: Int? = null,
    val conditions: List<EligibilityCondition>,
    val viewType: ViewType,
) : Model(id, title, drawableId) {

    data class EligibilityCondition(
        val title: String,
        val constraints: List<String>,
    )

    enum class ViewType {
        Card,
        Paragraph,
    }
}
