package com.samsung.healthcare.kit.step

import androidx.compose.runtime.Composable
import com.samsung.healthcare.kit.common.CallbackCollection
import com.samsung.healthcare.kit.model.Model
import com.samsung.healthcare.kit.view.View

/**
 * An object representing an action(=a single page)
 * such as Intro page.
 *
 * It maps a [Model][com.samsung.healthcare.kit.model.Model] and a [View][com.samsung.healthcare.kit.view.View].
 *
 * Then, View renders UI with data of Model.
 *
 * @property id id
 * @property name name
 * @property model data object for UI & state management
 * @property view view object holding UI method
 * @property getResult a method returning step's result
 */
abstract class Step<T : Model, R>(
    val id: String,
    val name: String,
    val model: T,
    val view: View<T>,
    val getResult: () -> R,
) {
    /**
     * A method for rendering UI.
     *
     * It triggers View's Render function.
     *
     * @param callbackCollection an object holding callback functions
     * @see [CallbackCollection]
     */
    @Composable
    abstract fun Render(callbackCollection: CallbackCollection)

    /**
     * A method for getting state of Step.
     *
     * Step's state is represented by its model.
     */
    fun getState(): T = model

    var result: R = getResult()
}
