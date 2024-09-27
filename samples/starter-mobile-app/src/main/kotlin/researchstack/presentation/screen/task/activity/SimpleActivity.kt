package researchstack.presentation.screen.task.activity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import researchstack.R
import researchstack.R.string
import researchstack.domain.model.task.ActivityTask
import researchstack.domain.model.task.activity.ActivityInstruction
import researchstack.domain.model.task.activity.ActivityResult
import researchstack.domain.model.task.activity.SimpleActivityMeasure
import researchstack.presentation.viewmodel.task.ActivityTaskViewModel

@Composable
fun SimpleActivity(
    task: ActivityTask,
    instructionImageId: Int,
    measureTimeSecond: Long,
    activityTaskViewModel: ActivityTaskViewModel,
    onComplete: () -> Unit,
) {
    when (activityTaskViewModel.phase.collectAsState().value) {
        ActivityTaskViewModel.Phase.Instruction ->
            ActivityInstructionScreen(
                ActivityInstruction(
                    task.title,
                    instructionImageId,
                    task.title,
                    listOf(task.description),
                    stringResource(id = string.start_assessment),
                )
            ) {
                activityTaskViewModel.setPhase(ActivityTaskViewModel.Phase.Measuring)
            }
        ActivityTaskViewModel.Phase.Measuring ->
            SimpleActivityMeasureScreen(
                SimpleActivityMeasure(
                    task.title,
                    R.drawable.ic_empty,
                    "",
                    listOf(),
                    stringResource(id = string.stop_assessment),
                    measureTimeSecond,
                )
            ) { result ->
                activityTaskViewModel.setPhase(ActivityTaskViewModel.Phase.Result)
                activityTaskViewModel.result = result
            }
        ActivityTaskViewModel.Phase.Result ->
            ActivityResultScreen(
                ActivityResult(
                    task.title,
                    R.drawable.ic_activity_result,
                    task.completionTitle,
                    listOf(task.completionDescription),
                    stringResource(id = string.task_done),
                ),
                activityTaskViewModel.taskState.collectAsState().value,
            ) {
                onComplete()
            }
    }
}
