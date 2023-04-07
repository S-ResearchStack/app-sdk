package healthstack.kit.task.activity.model

import healthstack.kit.R
import healthstack.kit.task.activity.model.common.SimpleViewActivityModel
import healthstack.kit.ui.TextType
import healthstack.kit.ui.TextType.NUMBER

class GaitAndBalanceIntroModel(
    id: String,
    title: String = "Gait & Balance",
    header: String = "Gait & Balance",
    body: List<String>? = listOf(
        "Walk unassisted for 20 steps in a straight line.",
        "Turn around and walk back to your starting point.\n",
        "Stand still for 20 seconds."
    ),
    drawableId: Int? = R.drawable.ic_activity_gait_and_balance_straight,
    buttonText: String? = "Begin", // If null, do not render bottom button
    textType: TextType = NUMBER,
) :
    SimpleViewActivityModel(
        id, title, header, body, drawableId, buttonText, textType
    )
