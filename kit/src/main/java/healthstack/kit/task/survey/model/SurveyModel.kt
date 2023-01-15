package healthstack.kit.task.survey.model

import healthstack.kit.task.base.StepModel

open class SurveyModel(
    id: String,
    title: String,
    drawableId: Int? = null,
) : StepModel(id, title, drawableId)
