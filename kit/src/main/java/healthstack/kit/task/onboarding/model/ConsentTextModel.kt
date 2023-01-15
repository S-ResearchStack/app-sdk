package healthstack.kit.task.onboarding.model

import healthstack.kit.task.base.StepModel

class ConsentTextModel(
    id: String,
    title: String,
    val subTitle: String,
    val description: String,
    val checkBoxTexts: List<String>,
    drawableId: Int? = null,
) : StepModel(id, title, drawableId) {
    var encodedSignature: String = ""
    val selections: BooleanArray = BooleanArray(checkBoxTexts.size)

    fun isAllChecked() = selections.all { it }
}
