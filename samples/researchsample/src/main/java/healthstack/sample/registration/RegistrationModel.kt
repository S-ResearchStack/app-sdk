package healthstack.sample.registration

import healthstack.kit.task.base.StepModel
import healthstack.kit.task.survey.question.model.QuestionModel

class RegistrationModel(
    title: String,
    val eligibilityQuestions: List<QuestionModel<Any>>
) : StepModel("", title, null)
