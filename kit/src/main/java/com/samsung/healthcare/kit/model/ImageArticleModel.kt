package com.samsung.healthcare.kit.model

class ImageArticleModel(
    id: String,
    title: String,
    val description: String,
    drawableId: Int?,
) : Model(id, title, drawableId) {
    init {
        drawableId ?: throw IllegalArgumentException("drawableId should not be null.")
    }
}
