package healthstack.app

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import healthstack.app.status.StatusDataType
import healthstack.app.status.TaskStatus
import healthstack.app.viewmodel.HealthStatusViewModel
import healthstack.kit.ui.StatusCard

@Composable
fun StatusCards(
    dataTypeStatus: List<StatusDataType>,
    viewModel: healthstack.app.viewmodel.TaskViewModel,
) {
    if (dataTypeStatus.isEmpty()) return
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
            .padding(horizontal = 24.dp)
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        dataTypeStatus.forEach { dataType ->
            if (dataType is TaskStatus) {
                TaskStatusCard(dataType, viewModel)
            } else {
                HealthStatusCard(dataType, HealthStatusViewModel(dataType))
            }
            Spacer(modifier = Modifier.width(20.dp))
        }
    }
}

@Composable
fun TaskStatusCard(
    dataType: StatusDataType,
    viewModel: healthstack.app.viewmodel.TaskViewModel,
) {
    val upcomingTaskState = viewModel.activeTasks.collectAsState()
    StatusCard(
        dataType.getIcon(), "${upcomingTaskState.value.tasks.size}", "remaining"
    )
}

@Composable
fun HealthStatusCard(
    dataType: StatusDataType,
    vm: healthstack.app.viewmodel.HealthStatusViewModel,
) {
    val healthState = vm.healthState.collectAsState()
    StatusCard(dataType.getIcon(), "${healthState.value.state}", dataType.getUnitString())
}
