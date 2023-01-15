package healthstack.kit.task.base

/**
 * A data object for [Step][healthstack.kit.task.base.Step].
 *
 * It has properties representing Step's state such as title, selectedValue etc.
 *
 * And it also has functions calculating Step's result.
 *
 * It is passed to composable function of [View][healthstack.kit.task.base.View]
 * as parameter.
 *
 * Then, View renders UI using Model's data.
 *
 * @property id id
 * @property title a title of UI
 * @property drawableId a representative image for UI
 */
abstract class StepModel(
    val id: String,
    val title: String,
    val drawableId: Int?,
)
