package healthstack.kit.task.activity.model

import healthstack.kit.R
import healthstack.kit.task.activity.model.common.SimpleViewActivityModel
import healthstack.kit.ui.TextType

class GuidedBreathingIntroModel(
    id: String,
    title: String = "Guided Breathing",
    header: String = "Guided Breathing",
    body: List<String>? = listOf(
        "Sit upright and take 10 deep breaths in and out as loudly as you can.",
        "Do not hold your breath between inhale & exhale.",
        "Try your best to follow the breathing guidance.",
    ),
    drawableId: Int? = R.drawable.ic_activity_guided_breathing,
    buttonText: String? = "Begin",
    textType: TextType = TextType.NUMBER,
) :
    SimpleViewActivityModel(
        id, title, header, body, drawableId, buttonText, textType,
    )
