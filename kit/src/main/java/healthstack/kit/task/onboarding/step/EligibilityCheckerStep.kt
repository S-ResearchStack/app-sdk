package healthstack.kit.task.onboarding.step

import androidx.compose.runtime.Composable
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.onboarding.model.EligibilityCheckerModel
import healthstack.kit.task.onboarding.view.EligibilityCheckerView
import healthstack.kit.task.survey.question.QuestionSubStep
import healthstack.kit.task.survey.question.SubStepHolder
import healthstack.kit.task.survey.question.component.Component
import healthstack.kit.task.survey.question.component.QuestionComponent
import healthstack.kit.task.survey.question.model.QuestionModel
import healthstack.kit.task.survey.step.SurveyStep
import java.util.UUID

class EligibilityCheckerStep(
    id: String,
    name: String,
    model: EligibilityCheckerModel,
    view: EligibilityCheckerView = EligibilityCheckerView(),
    subStepHolder: SubStepHolder,
) : SurveyStep(id, name, model, view, subStepHolder) {
    @Composable
    override fun Render(callbackCollection: CallbackCollection): Unit =
        view.Render(model, callbackCollection, subStepHolder)

    class Builder(
        private val title: String,
    ) {
        private val questionnaireSubSteps = mutableListOf<QuestionSubStep<*, *>>()

        @Suppress("UNCHECKED_CAST")
        fun <R> addQuestions(questions: List<QuestionModel<R>>): Builder {
            questions.forEach { question ->
                questionnaireSubSteps.add(
                    QuestionSubStep(
                        UUID.randomUUID().toString(),
                        question.id,
                        question,
                        QuestionComponent.defaultComponentOf(question.type) as Component<QuestionModel<R>>
                    )
                )
            }
            return this
        }

        fun build(): EligibilityCheckerStep =
            EligibilityCheckerStep(
                id = UUID.randomUUID().toString(),
                name = "not used",
                model = EligibilityCheckerModel(UUID.randomUUID().toString(), title),
                subStepHolder = SubStepHolder(
                    "sub-step-holder",
                    "Sub-Step-Holder",
                    listOf(questionnaireSubSteps),
                )
            )
    }
}
