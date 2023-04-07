package healthstack.kit.task.activity.predefined

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import healthstack.kit.R
import healthstack.kit.R.string
import healthstack.kit.task.activity.ActivityTask
import healthstack.kit.task.activity.model.ColorWordChallengeIntroModel
import healthstack.kit.task.activity.model.ColorWordChallengeMeasureModel
import healthstack.kit.task.activity.model.ColorWordChallengeResultModel
import healthstack.kit.task.activity.step.ColorWordChallengeMeasureStep
import healthstack.kit.task.activity.step.common.SimpleViewActivityStep
import healthstack.kit.task.base.Step
import healthstack.kit.task.base.StepModel
import healthstack.kit.ui.TaskCard

class ColorWordChallengeActivityTask(
    id: String,
    taskId: String,
    name: String,
    description: String,
    completionTitle: String,
    completionDescription: List<String>?,
    steps: List<Step<out StepModel, *>> = listOf(
        SimpleViewActivityStep(
            id, name, ColorWordChallengeIntroModel(id, name)
        ),
        ColorWordChallengeMeasureStep(
            id, name, ColorWordChallengeMeasureModel(id, name),
        ),
        SimpleViewActivityStep(
            id, name, ColorWordChallengeResultModel(id, name, header = completionTitle, body = completionDescription)
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
            id = R.drawable.ic_activity_color_word_challenge,
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
