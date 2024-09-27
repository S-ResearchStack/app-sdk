package researchstack.presentation.screen.task.activity

import android.Manifest
import android.provider.Settings
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import researchstack.R
import researchstack.R.string
import researchstack.domain.model.task.ActivityTask
import researchstack.domain.model.task.activity.ActivityInstruction
import researchstack.domain.model.task.activity.ActivityResult
import researchstack.presentation.LocalNavController
import researchstack.presentation.PermissionChecker
import researchstack.presentation.viewmodel.task.ActivityTaskViewModel
import java.nio.file.Paths

@Composable
fun MobileSpirometryActivity(
    task: ActivityTask,
    instructionImageId: Int,
    activityTaskViewModel: ActivityTaskViewModel,
    onComplete: () -> Unit,
) {
    val isPermissionGranted = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val navController = LocalNavController.current
    PermissionChecker(listOf(Manifest.permission.RECORD_AUDIO), listOf(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)) {
        isPermissionGranted.value = true
    }

    when (activityTaskViewModel.phase.collectAsState().value) {
        ActivityTaskViewModel.Phase.Instruction -> {
            ActivityInstructionScreen(
                ActivityInstruction(
                    task.title,
                    instructionImageId,
                    task.title,
                    listOf(task.description),
                    stringResource(id = string.start_recording),
                )
            ) {
                if (isPermissionGranted.value) {
                    activityTaskViewModel.setPhase(ActivityTaskViewModel.Phase.Measuring)
                } else {
                    Toast.makeText(context, context.getString(string.permission_require_toast), Toast.LENGTH_SHORT).show()
                    navController.navigateUp()
                }
            }
        }
        ActivityTaskViewModel.Phase.Measuring ->
            AudioRecordScreen(
                task.title,
                "Breathe forcefully 3 times",
                listOf(
                    "Hold phone vertically 3 inches from mouth.",
                    "Task a deep breath and blow out fast, forcefully and as long as you can.",
                    "Repeat 3 times",
                ),
                stringResource(id = string.stop_recording),
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
                val localFilePath = Paths.get(activityTaskViewModel.result["localFilePath"] as String)
                val uploadObjectName = "tasks/${task.taskId}/${localFilePath.fileName}"

                activityTaskViewModel.result = activityTaskViewModel.result.toMutableMap().apply {
                    this["filePath"] = uploadObjectName
                }

                onComplete()

                CoroutineScope(Dispatchers.IO).launch {
                    activityTaskViewModel.uploadFile(task.studyId, localFilePath, uploadObjectName)
                    localFilePath.toFile().delete()
                }
            }
    }
}
