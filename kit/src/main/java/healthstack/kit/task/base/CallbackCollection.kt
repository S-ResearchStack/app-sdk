package healthstack.kit.task.base

/**
 * A object holding callback functions.
 *
 * Callbacks are defined in [Task][healthstack.kit.task.base.Task],
 * and then sent to [Step][healthstack.kit.task.base.Step]
 * and [View][healthstack.kit.task.base.View].
 *
 * Step and View can access or change properties of Task using provided callback functions.
 */
open class CallbackCollection {
    open fun prev(): Unit = Unit
    open fun next(): Unit = Unit

    open fun setEligibility(value: Boolean): Unit = Unit
    open fun getEligibility(): Boolean = true
}
