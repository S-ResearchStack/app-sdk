package healthstack.kit.task.survey.question

import androidx.compose.runtime.Composable
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.survey.question.component.Component
import healthstack.kit.task.survey.question.model.QuestionModel

class QuestionSubStep<T : QuestionModel<R>, R>(
    val id: String,
    val name: String,
    val model: T,
    private val component: Component<T>, // @Composable (T, () -> Unit) -> Unit,
) {
    @Composable
    fun Render(callbackCollection: CallbackCollection) {
        component.Render(model, callbackCollection)
    }

    fun getState(): T = model

    fun getResult(): R? = model.getResponse()
}
