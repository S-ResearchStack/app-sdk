package healthstack.kit.task.activity.model

import healthstack.kit.R
import healthstack.kit.task.activity.model.common.SimpleViewActivityModel
import healthstack.kit.ui.TextType

class ReactionTimeIntroModel(
    id: String,
    title: String = "Reaction Time",
    goal: String = "square",
    header: String = "Reaction Time",
    body: List<String>? = listOf(
        "Hold phone in your right hand.",
        "As soon as you see a \"${goal}\" appear, shake phone.",
        "If you do not shake phone within 3 seconds of the ${goal}\'s appearing," +
            " you will need to try again."
    ),
    drawableId: Int? = R.drawable.ic_activity_reaction_time,
    buttonText: String? = "Begin",
    textType: TextType = TextType.NUMBER,
) :
    SimpleViewActivityModel(
        id, title, header, body, drawableId, buttonText, textType
    )
