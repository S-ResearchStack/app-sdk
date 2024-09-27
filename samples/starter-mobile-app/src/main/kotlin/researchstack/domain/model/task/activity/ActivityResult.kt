package researchstack.domain.model.task.activity

import researchstack.R

data class ActivityResult(
    val title: String,
    val imageId: Int = R.drawable.ic_activity_result,
    val header: String,
    val description: List<String>,
    val buttonText: String,
)
