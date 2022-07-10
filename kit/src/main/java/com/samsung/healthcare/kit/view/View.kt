package com.samsung.healthcare.kit.view

import androidx.compose.runtime.Composable
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.Model
import com.samsung.healthcare.kit.step.sub.SubStepHolder

/**
 * A UI rendering object for [Step][com.samsung.healthcare.kit.step.Step].
 *
 * It has composable function [Render],
 * which renders UI using data in [Model][com.samsung.healthcare.kit.model.Model].
 *
 * It looks similar with [Component][com.samsung.healthcare.kit.view.component.Component].
 *
 * But View can render a whole page unlike Component.
 */
abstract class View<T : Model> {
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
