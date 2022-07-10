package com.samsung.healthcare.kit.model

class RegistrationCompletedModel(
    id: String,
    title: String,
    val buttonText: String,
    val description: String,
    drawableId: Int,
) : Model(id, title, drawableId) {
    fun toImageArticleModel() =
        ImageArticleModel(
            id,
            title,
            description,
            drawableId
        )
}
