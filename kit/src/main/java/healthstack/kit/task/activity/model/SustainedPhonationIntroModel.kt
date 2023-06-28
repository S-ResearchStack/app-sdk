package healthstack.kit.task.activity.model

import healthstack.kit.R
import healthstack.kit.task.activity.model.common.SimpleAudioActivityModel
import healthstack.kit.ui.TextType

class SustainedPhonationIntroModel(
    id: String,
    title: String = "Sustained Phonation",
    header: String = "Sustained Phonation",
    body: List<String>? = listOf(
        "Find yourself in a quiet environment without background noise.",
        "Hold phone 6 inches from mouth.",
        "Inhale, then exhale with a loud \"ahh\"."
    ),
    drawableId: Int? = R.drawable.ic_activity_sustained_phonation,
    buttonText: String? = "Begin",
    textType: TextType = TextType.NUMBER,
) :
    SimpleAudioActivityModel(
        id, title, header, body, drawableId, buttonText, textType
    )
