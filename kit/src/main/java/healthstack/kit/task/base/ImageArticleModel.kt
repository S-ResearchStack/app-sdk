package healthstack.kit.task.base

/**
 * A [StepModel] with image.
 */
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
