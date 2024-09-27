package researchstack.presentation.screen.task.activity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import researchstack.R
import researchstack.R.string
import researchstack.domain.model.task.ActivityTask
import researchstack.domain.model.task.ActivityType
import researchstack.domain.model.task.activity.ActivityInstruction
import researchstack.domain.model.task.activity.ActivityResult
import researchstack.presentation.viewmodel.task.ActivityTaskViewModel
import java.time.OffsetDateTime

@Composable
fun LinkedWearableActivity(
    task: ActivityTask,
    measurementList: List<ActivityType>,
    instructionImageId: Int,
    activityTaskViewModel: ActivityTaskViewModel,
    onComplete: () -> Unit,
) {
    var idx by remember { mutableStateOf(0) }
    val startTime by remember { mutableStateOf(OffsetDateTime.now()) }

    val curMeasurementType = measurementList[idx]

    when (activityTaskViewModel.phase.collectAsState().value) {
        ActivityTaskViewModel.Phase.Instruction ->
            ActivityInstructionScreen(
                ActivityInstruction(
                    task.title,
                    instructionImageId,
                    measurementName(curMeasurementType),
                    listOf(task.description),
                    stringResource(id = string.start_task),
                )
            ) {
                activityTaskViewModel.setPhase(ActivityTaskViewModel.Phase.Measuring)
                CoroutineScope(Dispatchers.IO).launch {
                    activityTaskViewModel.launchWearableActivity(curMeasurementType)
                }
            }
        ActivityTaskViewModel.Phase.Measuring ->
            ActivityInstructionScreen(
                ActivityInstruction(
                    measurementName(curMeasurementType),
                    R.drawable.ic_empty,
                    "",
                    listOf("Continue on watch, and follow instructions there"),
                    stringResource(id = string.stop_task),
                )
            ) { result ->
                activityTaskViewModel.setPhase(ActivityTaskViewModel.Phase.Result)
            }
        ActivityTaskViewModel.Phase.Result ->
            ActivityResultScreen(
                ActivityResult(
                    measurementName(curMeasurementType),
                    R.drawable.ic_activity_result,
                    task.completionTitle,
                    listOf(task.completionDescription),
                    stringResource(id = string.task_done),
                ),
                activityTaskViewModel.taskState.collectAsState().value,
            ) {
                if (idx == measurementList.size - 1) {
                    activityTaskViewModel.result = mapOf(
                        "startTime" to startTime.toString(),
                        "endTime" to OffsetDateTime.now().toString(),
                    )
                    onComplete()
                } else {
                    activityTaskViewModel.setPhase(ActivityTaskViewModel.Phase.Instruction)
                    idx += 1
                }
            }
    }
}

fun measurementName(type: ActivityType): String {
    return when (type) {
        ActivityType.BIA_MEASUREMENT -> "Body Composition"
        ActivityType.BP_MEASUREMENT -> "Blood Pressure"
        ActivityType.ECG_MEASUREMENT -> "ECG"
        ActivityType.PPG_MEASUREMENT -> "PPG"
        ActivityType.SPO2_MEASUREMENT -> "Blood Oxygen"
        else -> throw Exception("not supported activity type")
    }
}
