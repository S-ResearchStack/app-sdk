package healthstack.kit.task.activity.model

import healthstack.kit.R
import healthstack.kit.task.activity.model.common.SimpleViewActivityModel
import healthstack.kit.ui.TextType
import healthstack.kit.ui.TextType.NUMBER

class TappingSpeedIntroModel(
    id: String,
    title: String = "Tapping Speed",
    header: String = "Tapping Speed",
    body: String? = "Place your phone on a flat surface.\n" +
        "Use two fingers on right hand to alternatively tap the buttons on the screen for 10 seconds.\n" +
        "Then, use two fingers on left hand to alternatively tap the buttons on the screen for 10 seconds.",
    drawableId: Int? = R.drawable.ic_right_tapping_speed,
    buttonText: String? = "Begin",
    textType: TextType = NUMBER,
    private val rightHand: Boolean = true,
) : SimpleViewActivityModel(
    id, title, header, body?.split("\n"), drawableId, buttonText, textType
) {
    val handType: String = when (rightHand) {
        true -> "right"
        else -> "left"
    }
}
