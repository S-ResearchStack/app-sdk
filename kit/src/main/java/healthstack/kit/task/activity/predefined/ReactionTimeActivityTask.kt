package healthstack.kit.task.activity.predefined

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import healthstack.kit.R
import healthstack.kit.R.string
import healthstack.kit.task.activity.ActivityTask
import healthstack.kit.task.activity.model.ReactionTimeIntroModel
import healthstack.kit.task.activity.model.ReactionTimeMeasureModel
import healthstack.kit.task.activity.model.ReactionTimeResultModel
import healthstack.kit.task.activity.step.ReactionTimeMeasureStep
import healthstack.kit.task.activity.step.common.SimpleViewActivityStep
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.StepModel
import healthstack.kit.ui.TaskCard

class ReactionTimeActivityTask(
    id: String,
    taskId: String,
    name: String,
    description: String,
    completionTitle: String,
    completionDescription: List<String>?,
    steps: List<Step<out StepModel, *>> = listOf(
        SimpleViewActivityStep(
            id, name, ReactionTimeIntroModel(id, name),
        ),
        ReactionTimeMeasureStep(
            id, name, ReactionTimeMeasureModel(id, name),
        ),
        SimpleViewActivityStep(
            id, name, ReactionTimeResultModel(id, name, header = completionTitle, body = completionDescription),
        ),
    ),
    isCompleted: Boolean = false,
    isActive: Boolean = true,
) : ActivityTask(id, taskId, name, description, steps) {
    init {
        this.isCompleted = isCompleted
        this.isActive = isActive
    }

    @Composable
    override fun CardView(onClick: () -> Unit) {
        TaskCard(
            id = R.drawable.ic_activity_reaction_time,
            taskName = name,
            description = description,
            isActive = isActive,
            buttonText = LocalContext.current.getString(string.start_task),
            isCompleted = isCompleted,
        ) {
            onClick()
        }
    }
}
