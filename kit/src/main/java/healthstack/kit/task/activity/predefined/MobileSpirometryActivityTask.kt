package healthstack.kit.task.activity.predefined

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import healthstack.kit.R
import healthstack.kit.task.activity.ActivityTask
import healthstack.kit.task.activity.model.MobileSpirometryIntroModel
import healthstack.kit.task.activity.model.MobileSpirometryMeasureModel
import healthstack.kit.task.activity.model.MobileSpirometryResultModel
import healthstack.kit.task.activity.step.MobileSpirometryMeasureStep
import healthstack.kit.task.activity.step.common.SimpleAudioActivityStep
import healthstack.kit.task.activity.step.common.SimpleViewActivityStep
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.StepModel
import healthstack.kit.ui.TaskCard

class MobileSpirometryActivityTask(
    id: String,
    taskId: String,
    name: String,
    description: String,
    completionTitle: String,
    completionDescription: List<String>?,
    steps: List<Step<out StepModel, *>> = listOf(
        SimpleAudioActivityStep(
            id, name, MobileSpirometryIntroModel(id, name)
        ),
        MobileSpirometryMeasureStep(
            id, name, MobileSpirometryMeasureModel(id, name)
        ),
        SimpleViewActivityStep(
            id, name, MobileSpirometryResultModel(id, name, header = completionTitle, body = completionDescription)
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
            id = R.drawable.ic_activity_mobile_spirometry,
            taskName = name,
            description = description,
            isActive = isActive,
            buttonText = LocalContext.current.getString(R.string.start_task),
            isCompleted = isCompleted,
        ) {
            onClick()
        }
    }
}
