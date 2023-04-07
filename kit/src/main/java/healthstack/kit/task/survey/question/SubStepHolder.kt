package healthstack.kit.task.survey.question

import androidx.compose.runtime.Composable
import healthstack.kit.task.base.CallbackCollection

/**
 * An object for holding one or more [SubSteps]
 *
 * SubStep's [Component][healthstack.kit.view.component.Component]
 * only renders a partial UI component.
 *
 * So, a Component has to depend on higher UI object.
 *
 * SubStepHolder helps managing SubSteps, and integrating UIs of Components
 *
 * For example, multiple QuestionSubSteps can be managed by a SubStepHolder.
 *
 * @property id id
 * @property name name
 * @property subSteps a list of subSteps to manage
 */
class SubStepHolder(
    val id: String,
    val name: String,
    val subSteps: List<List<QuestionSubStep<*, *>>>,
) {
    /**
     * A method for rendering UI.
     *
     * It triggers each Component's Render function,
     * and integrate them by defined logic.
     *
     * @param callbackCollection an object holding callback functions
     * @see [CallbackCollection]
     */
    @Composable
    fun Render(callbackCollection: CallbackCollection, index: Int) {
        subSteps[index].forEach {
            it.Render(callbackCollection)
        }
    }

    /**
     * A method to get result using its subSteps.
     */
    fun isSufficient(): Boolean = subSteps.all { section ->
        section.all {
            it.model.isCorrect()
        }
    }
}
