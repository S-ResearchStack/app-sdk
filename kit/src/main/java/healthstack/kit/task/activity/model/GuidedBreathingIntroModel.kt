package healthstack.kit.task.activity.model

import healthstack.kit.R
import healthstack.kit.task.activity.model.common.SimpleViewActivityModel
import healthstack.kit.ui.TextType

class GuidedBreathingIntroModel(
    id: String,
    title: String = "Guided Breathing",
    header: String = "Guided Breathing",
    body: List<String>? = listOf(
        "Sit upright and take 3 deep breaths in and out as loudly as you can.",
        "Inhale for 5 seconds and exhale for 5 seconds. Then, repeat for 3 cycles.",
        "Do not hold your breath between inhale & exhale.",
    ),
    drawableId: Int? = R.drawable.ic_activity_guided_breathing,
    buttonText: String? = "Begin",
    textType: TextType = TextType.NUMBER,
) :
    SimpleViewActivityModel(
        id, title, header, body, drawableId, buttonText, textType,
    )
