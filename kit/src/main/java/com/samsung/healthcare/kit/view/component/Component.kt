package com.samsung.healthcare.kit.view.component

import androidx.compose.runtime.Composable
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.Model

/**
 * A UI rendering object for SubStep such as [QuestionSubStep][com.samsung.healthcare.kit.step.sub.QuestionSubStep].
 *
 * It has composable function [Render],
 * which renders UI using data in [Model][com.samsung.healthcare.kit.model.Model].
 *
 * It looks similar with [View][com.samsung.healthcare.kit.view.View].
 *
 * But Component cannot render a whole page unlike View.
 *
 * Component can only render a partial "UI component" such as one question in a survey
 */
abstract class Component<T : Model> {
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
