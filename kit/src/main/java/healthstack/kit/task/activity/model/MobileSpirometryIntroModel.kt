package healthstack.kit.task.activity.model

import healthstack.kit.R
import healthstack.kit.task.activity.model.common.SimpleAudioActivityModel
import healthstack.kit.ui.TextType

class MobileSpirometryIntroModel(
    id: String,
    title: String = "Mobile Spirometry",
    count: Int = 3,
    header: String = "Mobile Spirometry",
    body: List<String>? = listOf(
        "Hold phone 6 inches from mouth.",
        "Take a deep breath and blow out fast, forcefully, and as long as you can.",
        "Repeat $count times in one recording."
    ),
    drawableId: Int? = R.drawable.ic_activity_mobile_spirometry,
    buttonText: String? = "Start Recording",
    textType: TextType = TextType.NUMBER,
) :
    SimpleAudioActivityModel(
        id, title, header, body, drawableId, buttonText, textType
    )
