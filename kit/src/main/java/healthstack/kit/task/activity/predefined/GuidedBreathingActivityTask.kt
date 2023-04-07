package healthstack.kit.task.activity.predefined

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import healthstack.kit.R
import healthstack.kit.R.string
import healthstack.kit.task.activity.ActivityTask
import healthstack.kit.task.activity.model.GuidedBreathingIntroModel
import healthstack.kit.task.activity.model.GuidedBreathingMeasureModel
import healthstack.kit.task.activity.model.GuidedBreathingResultModel
import healthstack.kit.task.activity.step.GuidedBreathingMeasureStep
import healthstack.kit.task.activity.step.common.SimpleViewActivityStep
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.StepModel
import healthstack.kit.ui.TaskCard

class GuidedBreathingActivityTask(
    id: String,
    taskId: String,
    name: String,
    description: String,
    completionTitle: String,
    completionDescription: List<String>?,
    steps: List<Step<out StepModel, *>> = listOf(
        SimpleViewActivityStep(
            id, name, GuidedBreathingIntroModel(id, name),
        ),
        GuidedBreathingMeasureStep(
            id, name, GuidedBreathingMeasureModel(id, name),
        ),
        SimpleViewActivityStep(
            id, name, GuidedBreathingResultModel(id, name, header = completionTitle, body = completionDescription),
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
            id = R.drawable.ic_activity_guided_breathing,
            taskName = name,
            description = description,
            isCompleted = isCompleted,
            isActive = isActive,
            buttonText = LocalContext.current.getString(string.start_task)
        ) {
            onClick()
        }
    }
}
