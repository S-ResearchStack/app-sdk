package com.samsung.healthcare.kit.common

/**
 * A object holding callback functions.
 *
 * Callbacks are defined in [Task][com.samsung.healthcare.kit.task.Task],
 * and then sent to [Step][com.samsung.healthcare.kit.step.Step]
 * and [View][com.samsung.healthcare.kit.view.View].
 *
 * Step and View can access or change properties of Task using provided callback functions.
 */
open class CallbackCollection {
    open fun prev(): Unit = Unit
    open fun next(): Unit = Unit

    open fun setEligibility(value: Boolean): Unit = Unit
    open fun getEligibility(): Boolean = true
}
