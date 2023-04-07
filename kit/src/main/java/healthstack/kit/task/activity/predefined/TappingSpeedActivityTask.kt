package healthstack.kit.task.activity.predefined

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import healthstack.kit.R
import healthstack.kit.R.string
import healthstack.kit.task.activity.ActivityTask
import healthstack.kit.task.activity.model.TappingSpeedIntroModel
import healthstack.kit.task.activity.model.TappingSpeedMeasureModel
import healthstack.kit.task.activity.model.TappingSpeedResultModel
import healthstack.kit.task.activity.step.TappingSpeedMeasureStep
import healthstack.kit.task.activity.step.common.SimpleViewActivityStep
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.StepModel
import healthstack.kit.ui.TaskCard

class TappingSpeedActivityTask(
    id: String,
    taskId: String,
    name: String = "Tapping Speed",
    description: String,
    completionTitle: String,
    completionDescription: List<String>?,
    steps: List<Step<out StepModel, *>> = listOf(
        SimpleViewActivityStep(
            id, name,
            TappingSpeedIntroModel(
                id, name
            )
        ),
        SimpleViewActivityStep(
            id, name,
            TappingSpeedIntroModel(
                id, name,
                body = "Place your phone on a flat surface.\n" +
                    "Use two fingers on right hand to alternatively tap the buttons on the screen.\n" +
                    "Keep tapping for 10 seconds.",
                buttonText = "Start Exercise"
            )
        ),
        TappingSpeedMeasureStep(
            id, name,
            TappingSpeedMeasureModel(
                id, name, null, measureTimeSecond = 10
            )
        ),
        SimpleViewActivityStep(
            id, name,
            TappingSpeedIntroModel(
                id, name,
                body = "Place your phone on a flat surface.\n" +
                    "Use two fingers on left hand to alternatively tap the buttons on the screen.\n" +
                    "Keep tapping for 10 seconds.",
                drawableId = R.drawable.ic_left_tapping_speed,
                buttonText = "Start Exercise"
            )
        ),
        TappingSpeedMeasureStep(
            id, name,
            TappingSpeedMeasureModel(
                id, name, null, false, 10
            )
        ),
        SimpleViewActivityStep(
            id, name,
            TappingSpeedResultModel(
                id, name, header = completionTitle, body = completionDescription
            )
        )
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
            id = R.drawable.ic_right_tapping_speed,
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
