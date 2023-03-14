package healthstack.kit.task.survey

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import healthstack.kit.R
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.Task
import healthstack.kit.task.survey.model.SurveyModel
import healthstack.kit.task.survey.question.QuestionSubStep
import healthstack.kit.task.survey.question.SubStepHolder
import healthstack.kit.task.survey.question.component.Component
import healthstack.kit.task.survey.question.component.QuestionComponent
import healthstack.kit.task.survey.question.model.QuestionModel
import healthstack.kit.task.survey.step.SurveyStep
import healthstack.kit.task.survey.view.SurveyView
import healthstack.kit.ui.TaskCard
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.util.UUID

open class SurveyTask private constructor(
    id: String,
    val revisionId: Int,
    val taskId: String,
    name: String,
    description: String,
    val step: SurveyStep,
    private var isActive: Boolean = true,
) : Task(
    id,
    name,
    description,
) {
    var startedAt: LocalDateTime? = null

    @Composable
    override fun Render() {
        startedAt = now()
        step.Render(
            callbackCollection = object : CallbackCollection() {
                override fun next() {
                    isCompleted = true
                    callback?.invoke()
                }

                override fun prev() {
                    canceled?.invoke()
                }
            }
        )
    }

    @Composable
    override fun CardView(onClick: () -> Unit) {
        TaskCard(
            taskName = name,
            description = description,
            isActive = isActive,
            isCompleted = isCompleted,
            buttonText = LocalContext.current.getString(R.string.start_task)
        ) {
            onClick()
        }
    }

    class Builder(
        private val id: String,
        private val revisionId: Int,
        private val taskId: String,
        private val name: String,
        private val description: String,
        private val callback: () -> Unit,
        private val pageable: Boolean = true,
        private val isCompleted: Boolean = false,
        private val isActive: Boolean = true,
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
                revisionId,
                taskId,
                name,
                description,
                SurveyStep(
                    UUID.randomUUID().toString(),
                    name,
                    SurveyModel(id, name),
                    SurveyView(pageable),
                    subStepHolder = SubStepHolder(id, name, subSteps)
                )
            ).apply {
                this.isCompleted = this@Builder.isCompleted
                this.isActive = this@Builder.isActive
            }
    }
}
