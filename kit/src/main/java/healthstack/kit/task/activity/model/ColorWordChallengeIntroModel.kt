package healthstack.kit.task.activity.model

import healthstack.kit.R
import healthstack.kit.task.activity.model.common.SimpleViewActivityModel
import healthstack.kit.ui.TextType

class ColorWordChallengeIntroModel(
    id: String,
    title: String = "Color Word Challenge",
    header: String = "Color Word Challenge",
    body: List<String>? = listOf(
        "See words presented in different colors",
        "Indicate the color in which each word is printed as quickly as you can.",
    ),
    drawableId: Int? = R.drawable.ic_activity_color_word_challenge,
    buttonText: String? = "Begin",
    textType: TextType = TextType.NUMBER,
) :
    SimpleViewActivityModel(
        id, title, header, body, drawableId, buttonText, textType,
    )
