package researchstack.presentation.screen.task

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import researchstack.domain.model.task.ActivityTask
import researchstack.domain.model.task.SurveyTask
import researchstack.presentation.viewmodel.task.TaskViewModel

@Composable
fun TaskScreen(
    taskId: Int,
    taskViewModel: TaskViewModel = hiltViewModel(),
) {
    LaunchedEffect(null) {
        taskViewModel.getTask(taskId)
    }

    taskViewModel.task.collectAsState().value
        ?.let { task ->
            when (task) {
                is ActivityTask -> ActivityTaskScreen(task)
                is SurveyTask -> SurveyTaskScreen(task)
            }
        }
}
