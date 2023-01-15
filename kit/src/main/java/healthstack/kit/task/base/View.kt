package healthstack.kit.task.base

import androidx.compose.runtime.Composable
import healthstack.kit.task.survey.question.SubStepHolder

/**
 * A UI rendering object for [Step][healthstack.kit.task.base.Step].
 *
 * It has composable function [Render],
 * which renders UI using data in [Model][healthstack.kit.tqask.base.TaskModel].
 *
 */
abstract class View<T : StepModel> {
    /**
     * A method rendering UI.
     *
     * @param model an object holding data to be rendered
     * @param callbackCollection an object holding callback functions
     * @param holder an container object managing sub Steps
     * @see [CallbackCollection]
     * @see [SubStepHolder]
     */
    @Composable
    abstract fun Render(
        model: T,
        callbackCollection: CallbackCollection,
        holder: SubStepHolder?,
    )
}
