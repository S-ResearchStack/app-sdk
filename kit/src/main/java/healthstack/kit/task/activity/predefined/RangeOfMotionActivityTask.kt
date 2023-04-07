package healthstack.kit.task.activity.predefined

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import healthstack.kit.R
import healthstack.kit.R.string
import healthstack.kit.task.activity.ActivityTask
import healthstack.kit.task.activity.model.RangeOfMotionIntroModel
import healthstack.kit.task.activity.model.RangeOfMotionMeasureModel
import healthstack.kit.task.activity.model.RangeOfMotionResultModel
import healthstack.kit.task.activity.step.RangeOfMotionMeasureStep
import healthstack.kit.task.activity.step.common.SimpleViewActivityStep
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.StepModel
import healthstack.kit.ui.TaskCard

class RangeOfMotionActivityTask(
    id: String,
    taskId: String,
    name: String,
    description: String,
    completionTitle: String,
    completionDescription: List<String>?,
    steps: List<Step<out StepModel, *>> = listOf(
        SimpleViewActivityStep(
            id, name, RangeOfMotionIntroModel(id, name)
        ),
        SimpleViewActivityStep(
            id,
            name,
            RangeOfMotionIntroModel(
                id = id,
                title = name,
                header = "Right Arm Circumduction",
                body = listOf(
                    "Place phone in your right hand.",
                    "Straighten your right arm and move it in a full circle for 20 sec."
                ),
                drawableId = R.drawable.ic_activity_range_of_motion_right_arm,
                buttonText = "Start Exercise",
            )
        ),
        RangeOfMotionMeasureStep(
            id, name, RangeOfMotionMeasureModel(id, name),
        ),
        SimpleViewActivityStep(
            id,
            name,
            RangeOfMotionResultModel(
                id,
                name,
                header = completionTitle,
                body = completionDescription,
                buttonText = "Continue"
            )
        ),
        SimpleViewActivityStep(
            id,
            name,
            RangeOfMotionIntroModel(
                id = id,
                title = name,
                header = "Left Arm Circumduction",
                body = listOf(
                    "Place phone in your left hand.",
                    "Straighten your left arm and move it in a full circle for 20 sec.",
                ),
                drawableId = R.drawable.ic_activity_range_of_motion_left_arm,
                buttonText = "Start Exercise",
            )
        ),
        RangeOfMotionMeasureStep(
            id,
            name,
            RangeOfMotionMeasureModel(
                id,
                name,
                header = "Left Arm Circumduction",
                body = listOf(
                    "Place phone in your left hand.",
                    "Straighten your left arm and move it in a full circle for 20 sec",
                ),
                isRightHand = false,
            )
        ),
        SimpleViewActivityStep(
            id,
            name,
            RangeOfMotionResultModel(
                id,
                name,
                header = completionTitle,
                body = completionDescription,
            )
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
            id = R.drawable.ic_activity_range_of_motion_right_arm,
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
