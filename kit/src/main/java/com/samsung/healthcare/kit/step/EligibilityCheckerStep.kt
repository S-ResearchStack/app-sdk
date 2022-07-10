package com.samsung.healthcare.kit.step

import androidx.compose.runtime.Composable
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.EligibilityCheckerModel
import com.samsung.healthcare.kit.model.question.QuestionModel
import com.samsung.healthcare.kit.step.sub.QuestionSubStep
import com.samsung.healthcare.kit.step.sub.SubStepHolder
import com.samsung.healthcare.kit.view.EligibilityCheckerView
import com.samsung.healthcare.kit.view.View
import com.samsung.healthcare.kit.view.component.Component
import com.samsung.healthcare.kit.view.component.QuestionComponent
import java.util.UUID

class EligibilityCheckerStep(
    id: String,
    name: String,
    model: EligibilityCheckerModel,
    view: View<EligibilityCheckerModel> = EligibilityCheckerView(),
    val subStepHolder: SubStepHolder,
) : Step<EligibilityCheckerModel, Boolean>(id, name, model, view, { true }) {
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
                    questionnaireSubSteps
                )
            )
    }
}
