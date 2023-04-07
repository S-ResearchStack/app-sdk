package healthstack.kit.task.base

import androidx.compose.runtime.Composable

/**
 * An object representing a flow of actions(= a list of pages)
 * such as survey flow or onBoarding flow.
 *
 * It usually has one or more [Step][healthstack.kit.task.base.Step]s as property.
 *
 * SimpleTask - Task with one Step.
 * OrderedTask - Task with multiple Steps.
 *
 * @property id id
 * @property name name
 * @property description concise description of task
 * @property isCompleted flag for completion of task
 * @property callback a method handling task's state when it is finished
 * @property canceled a method handling task's state when it is canceled
 */
abstract class Task(
    val id: String,
    val name: String,
    val description: String,
) {
    /**
     * A method for rendering UI.
     *
     * It triggers Step's Render method sequentially.
     */
    @Composable
    abstract fun Render()

    var isCompleted: Boolean = false

    var isActive: Boolean = true

    var callback: (() -> Unit)? = null

    var canceled: (() -> Unit)? = null

    /**
     * A method for rendering CardView UI.
     */
    @Composable
    abstract fun CardView(onClick: () -> Unit)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Task

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
