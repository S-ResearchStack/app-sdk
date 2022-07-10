package com.samsung.healthcare.kit.model

class EligibilityResultModel(
    id: String,
    title: String,

    drawableId: Int? = null,

    val successModel: ImageArticleModel,
    val failModel: ImageArticleModel,
) : Model(id, title, drawableId)
