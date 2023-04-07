package healthstack.kit.task.activity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import healthstack.kit.R.string
import healthstack.kit.task.base.CallbackCollection
import healthstack.kit.task.base.OrderedTask
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.StepModel
import healthstack.kit.ui.TaskCard
import java.time.LocalDateTime

abstract class ActivityTask(
    id: String,
    val taskId: String,
    name: String,
    description: String,
    steps: List<Step<out StepModel, *>>,
) : OrderedTask(
    id, name, description, steps
) {
    val result: MutableMap<String, Any> = mutableMapOf()
    var startedAt: LocalDateTime? = null

    override val pageCallbacks =
        object : CallbackCollection() {
            override fun next() {
                if (hasNext()) pageableStream.postValue(++progress.current)
                else {
                    callback?.invoke()
                }
            }

            override fun prev() {
                canceled?.invoke()
            }

            override fun setActivityResult(key: String, value: Any) {
                result[key] = value
            }

            override fun getActivityResult(): String = result.toString()
        }

    @Composable
    override fun Render() {
        val cursor: Int? by pageableStream.observeAsState()
        startedAt = LocalDateTime.now()
        steps[cursor ?: 0].Render(pageCallbacks)
    }

    @Composable
    override fun CardView(onClick: () -> Unit) {
        TaskCard(
            taskName = name,
            description = description,
            isActive = isActive,
            isCompleted = isCompleted,
            buttonText = LocalContext.current.getString(string.start_task)
        ) {
            onClick()
        }
    }
}
