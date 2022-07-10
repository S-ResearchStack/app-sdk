package com.samsung.healthcare.kit.model

import com.samsung.healthcare.kit.R

class IntroModel(
    id: String,
    title: String,
    drawableId: Int? = R.drawable.sample_image4,
    val logoDrawableId: Int? = null,
    val summaries: List<Pair<Int, String>>?,
    val descriptions: List<Pair<String, String>>,
) : Model(id, title, drawableId)
