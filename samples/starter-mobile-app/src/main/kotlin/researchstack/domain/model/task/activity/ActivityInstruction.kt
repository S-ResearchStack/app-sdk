package researchstack.domain.model.task.activity

data class ActivityInstruction(
    val title: String,
    val imageId: Int,
    val header: String,
    val description: List<String>,
    val buttonText: String,
)
