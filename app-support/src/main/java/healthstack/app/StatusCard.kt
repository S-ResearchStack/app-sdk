package healthstack.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import healthstack.app.status.HealthStatus
import healthstack.app.status.StatusDataType
import healthstack.app.status.TaskStatus
import healthstack.kit.theme.AppTheme
import healthstack.kit.ui.HealthDataStatusRow
import healthstack.kit.ui.TaskStatus

@Composable
fun StatusCards(
    dataTypeStatus: List<StatusDataType>,
    viewModel: healthstack.app.viewmodel.TaskViewModel,
) {
    if (dataTypeStatus.isEmpty()) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        val healthStatus = dataTypeStatus.filterNot {
            it is TaskStatus
        } as List<HealthStatus>

        HealthStatusCard(healthStatus)
        TaskStatusCard(dataTypeStatus.filterIsInstance<TaskStatus>()[0], viewModel)
    }
}

@Composable
fun TaskStatusCard(
    dataType: StatusDataType,
    viewModel: healthstack.app.viewmodel.TaskViewModel,
) {
    val upcomingTaskState = viewModel.activeTasks.collectAsState()

    TaskStatus(
        dataType.getIcon(), "${upcomingTaskState.value.tasks.size}", "TASKS\nREMAINING"
    )
}

@Composable
fun HealthStatusCard(
    data: List<HealthStatus>,
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val latestLifecycleEvent = remember { mutableStateOf(Lifecycle.Event.ON_ANY) }
    val viewModels = data.map {
        it.toViewModel()
    }

    val observer = LifecycleEventObserver { _, event ->
        latestLifecycleEvent.value = event
        viewModels.map {
            it.lifecycle = event
        }
    }

    lifecycle.addObserver(observer)

    val healthStates = viewModels.map {
        it.healthState.collectAsState()
    }

    Card(
        elevation = 2.dp,
        backgroundColor = AppTheme.colors.surface,
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier
            .size(152.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    vertical = 12.dp,
                    horizontal = 16.dp
                )
        ) {
            for (i in data.indices)
                HealthDataStatusRow(
                    data[i].getIcon(),
                    "${healthStates[i].value.state}",
                    data[i].getUnitString()
                )
        }
    }
}
