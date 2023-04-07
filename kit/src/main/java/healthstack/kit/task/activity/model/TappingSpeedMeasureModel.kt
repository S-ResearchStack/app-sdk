package healthstack.kit.task.activity.model

import healthstack.kit.task.base.StepModel

class TappingSpeedMeasureModel(
    id: String,
    title: String,
    drawableId: Int? = null,
    rightHand: Boolean = true,
    val measureTimeSecond: Int = 10,
) : StepModel(id, title, drawableId) {
    val handType: String = when (rightHand) {
        true -> "right"
        else -> "left"
    }
}
