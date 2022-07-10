package com.samsung.healthcare.kit.model

class ConsentTextModel(
    id: String,
    title: String,
    val subTitle: String,
    val description: String,
    val checkBoxTexts: List<String>,
    drawableId: Int? = null,
) : Model(id, title, drawableId) {
    var encodedSignature: String = ""
    val selections: BooleanArray = BooleanArray(checkBoxTexts.size)

    fun isAllChecked() = selections.all { it }
}
