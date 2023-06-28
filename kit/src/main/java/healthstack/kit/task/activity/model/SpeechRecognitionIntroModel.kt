package healthstack.kit.task.activity.model

import healthstack.kit.R
import healthstack.kit.task.activity.model.common.SimpleAudioActivityModel
import healthstack.kit.ui.TextType
import healthstack.kit.ui.TextType.NUMBER

class SpeechRecognitionIntroModel(
    id: String,
    title: String = "Speech Recognition",
    header: String = "Speech Recognition",
    body: List<String>? = listOf(
        "Find yourself in a quiet environment without background noise.",
        "Hold phone 6 inches from mouth.",
        "Read the displayed transcription as loudly as possible"
    ),
    drawableId: Int? = R.drawable.ic_activity_speech_recognition,
    buttonText: String? = "Begin",
    textType: TextType = NUMBER,
) : SimpleAudioActivityModel(
    id, title, header, body, drawableId, buttonText, textType
)
