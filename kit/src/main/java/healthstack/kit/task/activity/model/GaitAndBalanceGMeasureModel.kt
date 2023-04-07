package healthstack.kit.task.activity.model

import healthstack.kit.R
import healthstack.kit.task.activity.model.common.SimpleViewActivityModel

class GaitAndBalanceGMeasureModel(
    id: String,
    title: String = "Gait & Balance",
    header: String,
    body: List<String>? = null,
    drawableId: Int? = R.drawable.ic_activity_gait_and_balance_straight,
    buttonText: String? = null, // If null, do not render bottom button
) :
    SimpleViewActivityModel(
        id, title, header, body, drawableId, buttonText
    )
