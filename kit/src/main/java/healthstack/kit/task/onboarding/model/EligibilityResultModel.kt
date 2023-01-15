package healthstack.kit.task.onboarding.model

import healthstack.kit.task.base.ImageArticleModel
import healthstack.kit.task.base.StepModel

class EligibilityResultModel(
    id: String,
    title: String,

    drawableId: Int? = null,

    val successModel: ImageArticleModel,
    val failModel: ImageArticleModel,
) : StepModel(id, title, drawableId)
