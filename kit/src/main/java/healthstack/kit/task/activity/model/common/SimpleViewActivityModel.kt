package healthstack.kit.task.activity.model.common

import healthstack.kit.task.base.StepModel
import healthstack.kit.ui.TextType
import healthstack.kit.ui.TextType.PARAGRAPH

open class SimpleViewActivityModel(
    id: String,
    title: String,
    val header: String,
    val body: List<String>? = null,
    drawableId: Int? = null,
    val buttonText: String? = null, // If null, do not render bottom button
    val textType: TextType = PARAGRAPH,
) : StepModel(id, title, drawableId)
