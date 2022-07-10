package com.samsung.healthcare.kit.task

import androidx.compose.runtime.Composable
import com.samsung.healthcare.kit.R
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.EligibilityCheckerModel
import com.samsung.healthcare.kit.model.question.QuestionModel
import com.samsung.healthcare.kit.step.EligibilityCheckerStep
import com.samsung.healthcare.kit.step.sub.QuestionSubStep
import com.samsung.healthcare.kit.step.sub.SubStepHolder
import com.samsung.healthcare.kit.view.EligibilityCheckerView
import com.samsung.healthcare.kit.view.common.TaskCard
import com.samsung.healthcare.kit.view.component.Component
import com.samsung.healthcare.kit.view.component.QuestionComponent
import java.util.UUID

open class SurveyTask private constructor(
    id: String,
    name: String,
    description: String,
    val step: EligibilityCheckerStep,
) : Task(
    id,
    name,
    description,
) {
    @Composable
    override fun Render() {
        step.Render(
            callbackCollection = object : CallbackCollection() {
                override fun next() {
                    isCompleted = true
                    callback?.invoke()
                }
            }
        )
    }

    @Composable
    override fun CardView(onClick: () -> Unit) {
        TaskCard(
            id = R.drawable.ic_task,
            taskName = name,
            description = description,
            buttonEnabled = isCompleted.not()
        ) {
            onClick()
        }
    }

    class Builder(
        private val id: String,
        private val name: String,
        private val description: String,
        private val callback: () -> Unit,
        private val pageable: Boolean = true,
    ) {
        private val subSteps = mutableListOf<QuestionSubStep<*, *>>()

        @Suppress("UNCHECKED_CAST")
        fun <R> addQuestion(question: QuestionModel<R>) {
            addQuestion(
                question,
                QuestionComponent.defaultComponentOf(question.type) as Component<QuestionModel<R>>
            )
        }

        fun <R> addQuestion(question: QuestionModel<R>, component: Component<QuestionModel<R>>) {
            subSteps.add(
                QuestionSubStep(
                    // NOTE does step need id and name?
                    UUID.randomUUID().toString(),
                    UUID.randomUUID().toString(),
                    question,
                    component
                )
            )
        }

        fun build(): SurveyTask =
            SurveyTask(
                id,
                name,
                description,
                // TODO create another step or rename ?
                EligibilityCheckerStep(
                    UUID.randomUUID().toString(),
                    name,
                    EligibilityCheckerModel(id, name),
                    EligibilityCheckerView(pageable),
                    subStepHolder = SubStepHolder(id, name, subSteps)
                )
            )
    }
}
