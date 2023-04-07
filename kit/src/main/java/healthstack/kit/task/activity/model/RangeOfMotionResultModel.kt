package healthstack.kit.task.activity.model

import healthstack.kit.R
import healthstack.kit.task.activity.model.common.SimpleViewActivityModel
import healthstack.kit.ui.TextType

class RangeOfMotionResultModel(
    id: String,
    title: String = "Range of Motion",
    header: String = "Great Job!",
    body: List<String>? = listOf("Your right arm circumduction movement score has been recorded."),
    drawableId: Int? = R.drawable.ic_activity_result,
    buttonText: String? = "Back to Home",
    textType: TextType = TextType.PARAGRAPH,
) : SimpleViewActivityModel(id, title, header, body, drawableId, buttonText, textType)
