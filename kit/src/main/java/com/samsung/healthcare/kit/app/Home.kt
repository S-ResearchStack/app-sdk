package com.samsung.healthcare.kit.app

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.libraries.healthdata.data.DataType
import com.samsung.healthcare.kit.repository.RoomTaskRepository
import com.samsung.healthcare.kit.task.Task
import com.samsung.healthcare.kit.theme.AppTheme
import com.samsung.healthcare.kit.view.common.WeeklyCard
import com.samsung.healthcare.kit.viewmodel.TaskViewModel
import com.samsung.healthcare.kit.viewmodel.TaskViewModel.TasksState
import java.time.LocalDate

// TODO pass viewmodel, status info, ... into Home
private val roomdb: RoomTaskRepository = RoomTaskRepository()
val viewModel = TaskViewModel(roomdb)

@Composable
fun Home(dataTypeStatus: List<DataType>) {
    val scrollState = rememberScrollState()
    var selectedTask by remember {
        mutableStateOf<Task?>(null)
    }
    // TODO :poop: find a better way
    if (selectedTask == null) {
        DailyTaskView(LocalDate.now(), dataTypeStatus, viewModel, scrollState) { selectedTask = it }
    } else {
        selectedTask?.callback = {
            selectedTask?.let {
                viewModel.done(it)
            }
            selectedTask = null
        }
        selectedTask?.Render()
    }
}

@Composable
private fun DailyTaskView(
    date: LocalDate,
    dataTypeStatus: List<DataType>,
    viewModel: TaskViewModel,
    scrollState: ScrollState,
    onStartTask: (Task) -> Unit
) {

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(26.dp))

            WeeklyCard(date)
            Spacer(Modifier.height(32.dp))

            StatusCards(dataTypeStatus)
            Spacer(Modifier.height(40.dp))

            Tasks("Upcoming", viewModel.upcomingTasks.collectAsState().value) {
                onStartTask(it)
            }
            Spacer(Modifier.height(32.dp))
            Tasks("Completed", viewModel.completedTasks.collectAsState().value) { }
        }
    }
}

@Composable
fun Tasks(
    title: String,
    state: TasksState,
    onStartTask: (Task) -> Unit,
) {
    Column {
        Text(
            title,
            style = AppTheme.typography.title3,
            fontWeight = FontWeight.Bold,
            color = AppTheme.colors.textPrimary
        )
        Spacer(Modifier.height(16.dp))
        state.tasks.forEach {
            it.CardView {
                onStartTask(it)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
