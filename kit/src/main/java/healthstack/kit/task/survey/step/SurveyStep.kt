package healthstack.kit.task.survey.step

import androidx.compose.runtime.Composable
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.View
import healthstack.kit.task.survey.model.SurveyModel
import healthstack.kit.task.survey.question.QuestionSubStep
import healthstack.kit.task.survey.question.SubStepHolder
import healthstack.kit.task.survey.question.component.Component
import healthstack.kit.task.survey.question.component.QuestionComponent
import healthstack.kit.task.survey.question.model.QuestionModel
import healthstack.kit.task.survey.view.SurveyView
import java.util.UUID

open class SurveyStep(
    id: String,
    name: String,
    model: SurveyModel,
    view: View<SurveyModel> = SurveyView(),
    val subStepHolder: SubStepHolder,
) : Step<SurveyModel, Boolean>(id, name, model, view, { true }) {
    @Composable
    override fun Render(callbackCollection: CallbackCollection): Unit =
        view.Render(model, callbackCollection, subStepHolder)

    class Builder(
        private val title: String,
        private val view: View<SurveyModel> = SurveyView(),
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

        fun build(): SurveyStep =
            SurveyStep(
                id = UUID.randomUUID().toString(),
                name = "not used",
                model = SurveyModel(UUID.randomUUID().toString(), title),
                view = view,
                subStepHolder = SubStepHolder(
                    "sub-step-holder",
                    "Sub-Step-Holder",
                    listOf(questionnaireSubSteps),
                )
            )
    }
}
