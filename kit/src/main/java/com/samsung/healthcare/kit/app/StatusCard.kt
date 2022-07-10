package com.samsung.healthcare.kit.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.libraries.healthdata.data.DataType
import com.samsung.healthcare.kit.app.TaskDataType.Companion.TASK_DATA_TYPE
import com.samsung.healthcare.kit.view.common.StatusCard
import com.samsung.healthcare.kit.viewmodel.HealthStatusViewModel

// TODO should set types from outside and integrate with Health Platform
@Composable
fun StatusCards(dataTypeStatus: List<DataType>) {
    if (dataTypeStatus.isEmpty()) return
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        dataTypeStatus.forEach { dataType ->
            if (dataType == TASK_DATA_TYPE) {
                TaskStatusCard(dataType)
            } else {
                HealthStatusCard(dataType, HealthStatusViewModel(dataType))
            }
        }
    }
}

@Composable
fun TaskStatusCard(dataType: DataType) {
    val upcomingTaskState = viewModel.upcomingTasks.collectAsState()
    StatusCard(
        dataType.getIcon(),
        "${upcomingTaskState.value.tasks.size}${dataType.getPostfix()}"
    )
}

@Composable
fun HealthStatusCard(
    dataType: DataType,
    vm: HealthStatusViewModel
) {
    val healthState = vm.healthState.collectAsState()
    StatusCard(dataType.getIcon(), "${healthState.value.state}${dataType.getPostfix()}")
}
