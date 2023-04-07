package healthstack.kit.task.activity.model

import healthstack.kit.R
import healthstack.kit.task.activity.model.common.SimpleViewActivityModel

class TappingSpeedResultModel(
    id: String,
    title: String = "Tapping Speed",
    header: String = "Great Job!",
    body: List<String>? = listOf("Your task was successfully completed."),
    drawableId: Int? = R.drawable.ic_activity_result,
    buttonText: String? = "Back to Home",
) : SimpleViewActivityModel(
    id, title, header, body, drawableId, buttonText
)
