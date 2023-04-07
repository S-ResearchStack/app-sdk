package healthstack.kit.task.activity.model

import healthstack.kit.R
import healthstack.kit.task.activity.model.common.SimpleViewActivityModel
import healthstack.kit.ui.TextType

class ColorWordChallengeResultModel(
    id: String,
    title: String = "Color Word Challenge",
    header: String = "Great Job!",
    body: List<String>? = listOf("Your task was successfully completed."),
    drawableId: Int? = R.drawable.ic_activity_result,
    buttonText: String? = "Back to Home",
    textType: TextType = TextType.PARAGRAPH,
) :
    SimpleViewActivityModel(
        id, title, header, body, drawableId, buttonText, textType,
    )
