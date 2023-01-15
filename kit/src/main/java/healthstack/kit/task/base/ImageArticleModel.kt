package healthstack.kit.task.base

class ImageArticleModel(
    id: String,
    title: String,
    val description: String,
    drawableId: Int?,
) : StepModel(id, title, drawableId) {
    init {
        drawableId ?: throw IllegalArgumentException("drawableId should not be null.")
    }
}
