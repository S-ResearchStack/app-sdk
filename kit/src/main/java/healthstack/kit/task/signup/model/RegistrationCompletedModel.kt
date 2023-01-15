package healthstack.kit.task.signup.model

import healthstack.kit.task.base.ImageArticleModel
import healthstack.kit.task.base.StepModel

class RegistrationCompletedModel(
    id: String,
    title: String,
    val buttonText: String,
    val description: String,
    drawableId: Int,
) : StepModel(id, title, drawableId) {
    fun toImageArticleModel() =
        ImageArticleModel(
            id,
            title,
            description,
            drawableId
        )
}
