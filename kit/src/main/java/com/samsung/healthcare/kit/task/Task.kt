package com.samsung.healthcare.kit.task

import androidx.compose.runtime.Composable

/**
 * An object representing a flow of actions(= a list of pages)
 * such as survey flow or onBoarding flow.
 *
 * It usually has one or more [Step][com.samsung.healthcare.kit.step.Step]s as property.
 *
 * SimpleTask - Task with one Step.
 * OrderedTask - Task with multiple Steps.
 *
 * @property id id
 * @property name name
 * @property description concise description of task
 * @property callback a method returning task's state
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

    var callback: (() -> Unit)? = null

    var canceled: (() -> Unit)? = null

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
