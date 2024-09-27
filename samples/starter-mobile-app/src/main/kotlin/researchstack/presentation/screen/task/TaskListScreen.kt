package researchstack.presentation.screen.task

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import researchstack.R.drawable
import researchstack.R.string
import researchstack.domain.model.task.ActivityTask
import researchstack.domain.model.task.ActivityType
import researchstack.domain.model.task.SurveyTask
import researchstack.domain.model.task.Task
import researchstack.presentation.LocalNavController
import researchstack.presentation.component.TopBar
import researchstack.presentation.initiate.route.Route
import researchstack.presentation.theme.AppTheme
import researchstack.presentation.theme.descriptionColor
import researchstack.presentation.viewmodel.task.TaskListViewModel

@Composable
fun TaskListScreen(
    taskListViewModel: TaskListViewModel = hiltViewModel(),
) {
    LaunchedEffect(null) {
        taskListViewModel.getTasks()
    }
    val todayTasks = taskListViewModel.todayTasks.collectAsState().value
    val completedTasks = taskListViewModel.completedTasks.collectAsState().value

    Scaffold(
        modifier = Modifier.fillMaxSize(1f),
        topBar = {
            TopBar(title = "Health Research")
        },
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, top = 8.dp, end = 24.dp),
        ) {
            TaskCardList(todayTasks, true, string.active_task_title, string.no_active_task_message)

            Spacer(modifier = Modifier.height(32.dp))

            TaskCardList(completedTasks, false, string.completed_task_title, string.no_completed_task_message)
        }
    }
}

@Composable
fun TaskCardList(tasks: List<Task>, isActive: Boolean, @StringRes titleId: Int, @StringRes emptyMessageId: Int) {
    Text(
        text = stringResource(titleId),
        style = AppTheme.typography.title2
    )
    Spacer(modifier = Modifier.height(16.dp))

    if (tasks.isEmpty()) {
        NoTaskMessage(stringResource(id = emptyMessageId))
    } else {
        tasks.forEach {
            TaskCard(it, isActive)
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun NoTaskMessage(message: String) {
    Text(
        message,
        style = AppTheme.typography.body2,
        color = descriptionColor
    )
}

@Composable
fun TaskCard(task: Task, isActive: Boolean) {
    val shape = RoundedCornerShape(4.dp)
    val navController = LocalNavController.current
    val imageId = when (task) {
        is SurveyTask -> drawable.ic_task
        is ActivityTask -> {
            when (task.activityType) {
                ActivityType.UNSPECIFIED -> TODO()
                ActivityType.TAPPING_SPEED -> TODO()
                ActivityType.REACTION_TIME -> TODO()
                ActivityType.GUIDED_BREATHING -> drawable.ic_activity_guided_breathing
                ActivityType.RANGE_OF_MOTION -> TODO()
                ActivityType.GAIT_AND_BALANCE -> TODO()
                ActivityType.STROOP_TEST -> TODO()
                ActivityType.SPEECH_RECOGNITION -> TODO()
                ActivityType.MOBILE_SPIROMETRY -> drawable.ic_activity_mobile_spirometry
                ActivityType.SUSTAINED_PHONATION -> TODO()
                ActivityType.FIVE_METER_WALK_TEST -> drawable.ic_activity_five_meter_walk_test
                ActivityType.STATE_BALANCE_TEST -> drawable.ic_activity_stage_balance_test
                ActivityType.ROMBERG_TEST -> drawable.ic_activity_romberg_test
                ActivityType.SIT_TO_STAND -> drawable.ic_activity_sit_to_stand
                ActivityType.ORTHOSTATIC_BP -> drawable.ic_activity_orthostatic_bp
                ActivityType.BIA_MEASUREMENT -> drawable.ic_activity_bia_measurement
                ActivityType.BP_MEASUREMENT -> drawable.ic_activity_bp_measurement
                ActivityType.ECG_MEASUREMENT -> drawable.ic_activity_ecg_measurement
                ActivityType.PPG_MEASUREMENT -> drawable.ic_activity_ppg_measurement
                ActivityType.SPO2_MEASUREMENT -> drawable.ic_activity_spo2_measurement
                ActivityType.BP_AND_BIA_MEASUREMENT -> drawable.ic_activity_bp_and_bia_measurement
                ActivityType.STABLE_MEASUREMENT -> drawable.ic_activity_stable_measurement
                ActivityType.SHAPE_PAINTING -> TODO()
                ActivityType.CATCH_LADYBUG -> TODO()
                ActivityType.MEMORIZE -> TODO()
                ActivityType.MEMORIZE_WORDS_START -> TODO()
                ActivityType.MEMORIZE_WORDS_END -> TODO()
                ActivityType.DESCRIBE_IMAGE -> TODO()
                ActivityType.READ_TEXT_ALOUD -> TODO()
                ActivityType.ANSWER_VERBALLY -> TODO()
                ActivityType.ANSWER_WRITTEN -> TODO()
            }
        }
    }

    Card(
        shape = shape,
        backgroundColor = if (isActive) AppTheme.colors.surface else AppTheme.colors.disabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(91.dp)
            .clickable(isActive) {
                navController.navigate("${Route.Task.name}/${task.id}")
            }
            .shadow(elevation = 2.dp, shape = shape, clip = false),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TaskIcon(imageId)

                Spacer(modifier = Modifier.width(16.dp))

                TaskNameAndTime(task)
            }
        }
    }
}

@Composable
private fun TaskNameAndTime(task: Task) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = task.title,
            style = AppTheme.typography.title2,
            color = AppTheme.colors.onSurface,
        )

        Text(
            text = task.description,
            style = AppTheme.typography.body3,
            color = descriptionColor,
        )
    }
}

@Composable
private fun TaskIcon(imageId: Int) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(CircleShape)
            .background(AppTheme.colors.primary.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = "",
        )
    }
}
