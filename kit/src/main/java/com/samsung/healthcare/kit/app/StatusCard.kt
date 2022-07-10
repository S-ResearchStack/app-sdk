package com.samsung.healthcare.kit.app

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
import com.google.android.libraries.healthdata.data.DataType
import com.samsung.healthcare.kit.app.TaskDataType.Companion.TASK_DATA_TYPE
import com.samsung.healthcare.kit.view.common.StatusCard
import com.samsung.healthcare.kit.viewmodel.HealthStatusViewModel
import com.samsung.healthcare.kit.viewmodel.TaskViewModel

@Composable
fun StatusCards(
    dataTypeStatus: List<DataType>,
    viewModel: TaskViewModel,
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
            if (dataType == TASK_DATA_TYPE) {
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
    dataType: DataType,
    viewModel: TaskViewModel,
) {
    val upcomingTaskState = viewModel.activeTasks.collectAsState()
    StatusCard(
        dataType.getIcon(), "${upcomingTaskState.value.tasks.size}", "remaining"
    )
}

@Composable
fun HealthStatusCard(
    dataType: DataType,
    vm: HealthStatusViewModel,
) {
    val healthState = vm.healthState.collectAsState()
    StatusCard(dataType.getIcon(), "${healthState.value.state}", dataType.getPostfix())
}
