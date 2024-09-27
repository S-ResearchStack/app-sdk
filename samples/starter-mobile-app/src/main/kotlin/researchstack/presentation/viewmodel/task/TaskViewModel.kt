package researchstack.presentation.viewmodel.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import researchstack.domain.model.task.Task
import researchstack.domain.model.task.taskresult.TaskResult
import researchstack.domain.usecase.task.GetTodayTasksUseCase
import researchstack.domain.usecase.task.SaveAndUploadTaskResultUseCase
import researchstack.presentation.viewmodel.task.TaskViewModel.TaskState.Complete
import researchstack.presentation.viewmodel.task.TaskViewModel.TaskState.Init
import researchstack.presentation.viewmodel.task.TaskViewModel.TaskState.Saving
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
open class TaskViewModel @Inject constructor(
    // TODO need method for getting task by task id
    private val getTodayTasksUseCase: GetTodayTasksUseCase,
    private val saveTaskResultUseCase: SaveAndUploadTaskResultUseCase,
) : ViewModel() {

    private val _task = MutableStateFlow<Task?>(null)
    val task: StateFlow<Task?> = _task

    private val _taskState = MutableStateFlow(Init)
    val taskState: StateFlow<TaskState> = _taskState

    fun getTask(taskId: Int) {
        _task.value = null
        viewModelScope.launch {
            _task.value = getTodayTasksUseCase(LocalDateTime.now()).firstOrNull()?.let { tasks ->
                tasks.first { it.id == taskId }
            }
        }
    }

    fun saveTaskResult(taskResult: TaskResult) {
        _taskState.value = Saving
        viewModelScope.launch(Dispatchers.IO) {
            saveTaskResultUseCase(taskResult).onFailure {
                // TODO register worker to handle fail case
            }

            _taskState.value = Complete
        }
    }

    enum class TaskState {
        Init,
        Saving,
        Complete,
    }
}
