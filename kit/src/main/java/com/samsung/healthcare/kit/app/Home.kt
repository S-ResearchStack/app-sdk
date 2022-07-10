package com.samsung.healthcare.kit.app

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.libraries.healthdata.data.DataType
import com.samsung.healthcare.kit.task.Task
import com.samsung.healthcare.kit.theme.AppTheme
import com.samsung.healthcare.kit.view.common.WeeklyCard
import com.samsung.healthcare.kit.viewmodel.TaskViewModel
import com.samsung.healthcare.kit.viewmodel.TaskViewModel.TasksState
import java.time.LocalDate

@Composable
fun Home(
    dataTypeStatus: List<DataType>,
    viewModel: TaskViewModel,
) {
    val scrollState = rememberScrollState()
    var selectedTask by remember {
        mutableStateOf<Task?>(null)
    }

    if (selectedTask == null) {
        DailyTaskView(
            LocalDate.now(),
            dataTypeStatus,
            viewModel,
            scrollState,
        ) { selectedTask = it }
    } else {
        selectedTask?.let { task ->
            task.callback = {
                viewModel.done(task)
                selectedTask = null
            }
            task.canceled = {
                selectedTask = null
            }
            task.Render()
        }
    }
}

@Composable
private fun DailyTaskView(
    date: LocalDate,
    dataTypeStatus: List<DataType>,
    viewModel: TaskViewModel,
    scrollState: ScrollState,
    onStartTask: (Task) -> Unit,
) {
    Scaffold(
        backgroundColor = AppTheme.colors.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(26.dp))

            WeeklyCard(date)
            Spacer(Modifier.height(32.dp))

            StatusCards(dataTypeStatus, viewModel)
            Spacer(Modifier.height(40.dp))

            Tasks(
                "Active",
                viewModel.activeTasks.collectAsState().value,
                { viewModel.syncTasks() }
            ) {
                onStartTask(it)
            }
            Spacer(Modifier.height(32.dp))
            Tasks(
                "Today",
                viewModel.todayTasks.collectAsState().value, { }
            ) { }
            Spacer(Modifier.height(32.dp))
            Tasks(
                "Completed",
                viewModel.completedTasks.collectAsState().value, { }
            ) { }
        }
    }
}

@Composable
fun Tasks(
    title: String,
    state: TasksState,
    onReload: () -> Unit,
    onStartTask: (Task) -> Unit,
) {
    Column {
        Row(
            horizontalArrangement = SpaceBetween,
            verticalAlignment = CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                title,
                style = AppTheme.typography.title3,
                color = AppTheme.colors.textPrimary
            )
            IconButton(onClick = {
                onReload()
            }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    tint = AppTheme.colors.primary,
                    contentDescription = null,
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        state.tasks.forEach {
            it.CardView {
                onStartTask(it)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
