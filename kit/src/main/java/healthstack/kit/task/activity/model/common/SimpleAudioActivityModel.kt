package healthstack.kit.task.activity.model.common

import healthstack.kit.task.base.StepModel
import healthstack.kit.ui.TextType

open class SimpleAudioActivityModel(
    id: String,
    title: String,
    val header: String,
    val body: List<String>? = null,
    drawableId: Int? = null,
    val buttonText: String? = null,
    val textType: TextType = TextType.PARAGRAPH,
) : StepModel(id, title, drawableId)
