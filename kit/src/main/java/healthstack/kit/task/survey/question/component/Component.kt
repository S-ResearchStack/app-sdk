package healthstack.kit.task.survey.question.component

import androidx.compose.runtime.Composable
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.survey.question.model.QuestionModel

/**
 * A UI rendering object for SubStep such as [QuestionSubStep][healthstack.kit.step.sub.QuestionSubStep].
 *
 * It has composable function [Render],
 * which renders UI using data in [Model][healthstack.kit.model.Model].
 *
 * It looks similar with [View][healthstack.kit.view.View].
 *
 * But Component cannot render a whole page unlike View.
 *
 * Component can only render a partial "UI component" such as one question in a survey
 */
abstract class Component<T : QuestionModel<*>> {
    /**
     * A method rendering UI.
     *
     * @param model an object holding data to be rendered
     * @param callbackCollection an object holding callback functions
     * @see [CallbackCollection]
     */
    @Composable
    abstract fun Render(
        model: T,
        callbackCollection: CallbackCollection,
    )
}
