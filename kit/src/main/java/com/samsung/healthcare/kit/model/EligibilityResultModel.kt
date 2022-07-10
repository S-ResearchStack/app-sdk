package com.samsung.healthcare.kit.model

class EligibilityResultModel(
    id: String,
    title: String,

    drawableId: Int? = null,

    // TODO: naming..
    val successModel: ImageArticleModel,
    val failModel: ImageArticleModel,
) : Model(id, title, drawableId)
