package healthstack.kit.task.onboarding.model

import healthstack.kit.task.survey.model.SurveyModel

open class EligibilityCheckerModel(
    id: String,
    title: String,
    drawableId: Int? = null,
) : SurveyModel(id, title, drawableId)
