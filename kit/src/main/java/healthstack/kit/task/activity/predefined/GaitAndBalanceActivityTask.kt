package healthstack.kit.task.activity.predefined

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import healthstack.kit.R
import healthstack.kit.R.string
import healthstack.kit.task.activity.ActivityTask
import healthstack.kit.task.activity.model.GaitAndBalanceBMeasureModel
import healthstack.kit.task.activity.model.GaitAndBalanceGMeasureModel
import healthstack.kit.task.activity.model.GaitAndBalanceIntroModel
import healthstack.kit.task.activity.model.GaitAndBalanceResultModel
import healthstack.kit.task.activity.step.common.SimpleTimerActivityStep
import healthstack.kit.task.activity.step.common.SimpleViewActivityStep
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.StepModel
import healthstack.kit.ui.TaskCard
import healthstack.kit.ui.util.InteractionType.VIBRATE

class GaitAndBalanceActivityTask(
    id: String,
    taskId: String,
    name: String = "Gait & Balance",
    description: String,
    completionTitle: String,
    completionDescription: List<String>?,
    steps: List<Step<out StepModel, *>> = listOf(
        SimpleViewActivityStep(
            id, name,
            GaitAndBalanceIntroModel(id)
        ),
        SimpleViewActivityStep(
            id, name,
            GaitAndBalanceGMeasureModel(
                id,
                header = "Walk in a straight line unassisted for 20 steps",
                buttonText = "Done"
            )
        ),
        SimpleViewActivityStep(
            id, name,
            GaitAndBalanceGMeasureModel(
                id,
                header = "Turn around and walk back to your starting point",
                drawableId = R.drawable.ic_activity_gait_and_balance_back,
                buttonText = "Done"
            )
        ),
        SimpleTimerActivityStep(
            id, name,
            GaitAndBalanceBMeasureModel(
                id,
                header = "Stand still for 20 seconds",
                timeSeconds = 20,
                interactionType = VIBRATE,
            )
        ),
        SimpleViewActivityStep(
            id, name,
            GaitAndBalanceResultModel(id, name, header = completionTitle, body = completionDescription)
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
            id = R.drawable.ic_activity_gait_and_balance_straight,
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
