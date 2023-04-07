package healthstack.kit.task.activity.model

import healthstack.kit.R
import healthstack.kit.task.activity.model.common.SimpleViewActivityModel
import healthstack.kit.ui.TextType

class RangeOfMotionIntroModel(
    id: String,
    title: String = "Range of Motion",
    header: String = "Range of Motion",
    body: List<String>? = listOf(
        "Place phone in your right hand.",
        "Straighten your right arm and move it in a full circle for the given sec.",
        "Then, place phone in your left hand.",
        "Straighten your left arm and move it in a full circle for the given sec.",
    ),
    drawableId: Int? = R.drawable.ic_activity_range_of_motion_right_arm,
    buttonText: String? = "Begin",
    textType: TextType = TextType.NUMBER,
) : SimpleViewActivityModel(id, title, header, body, drawableId, buttonText, textType)
