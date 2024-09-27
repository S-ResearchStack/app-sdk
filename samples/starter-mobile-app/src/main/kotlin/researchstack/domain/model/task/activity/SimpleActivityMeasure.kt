package researchstack.domain.model.task.activity

import researchstack.util.InteractionType

data class SimpleActivityMeasure(
    val title: String,
    val imageId: Int,
    val header: String,
    val description: List<String>,
    val buttonText: String,
    val timeSeconds: Long,
    val interactionType: InteractionType = InteractionType.VIBRATE,
)
