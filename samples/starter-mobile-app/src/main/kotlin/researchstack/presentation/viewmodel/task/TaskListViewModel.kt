package researchstack.presentation.viewmodel.task

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import researchstack.data.datasource.local.pref.dataStore
import researchstack.domain.model.task.Task
import researchstack.domain.usecase.task.GetCompletedTasksUseCase
import researchstack.domain.usecase.task.GetTodayTasksUseCase
import researchstack.presentation.pref.UIPreference
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    application: Application,
    private val getTodayTasksUseCase: GetTodayTasksUseCase,
    private val getCompletedTasksUseCase: GetCompletedTasksUseCase,
) : AndroidViewModel(application) {
    private val _todayTasks = MutableStateFlow<List<Task>>(emptyList())
    val todayTasks: StateFlow<List<Task>> = _todayTasks

    private val _completedTasks = MutableStateFlow<List<Task>>(emptyList())
    val completedTasks: StateFlow<List<Task>> = _completedTasks

    private val pagePreference = UIPreference(application.dataStore)

    private val localDateTimeFlow: Flow<LocalDateTime> = flow {
        while (true) {
            emit(LocalDateTime.now())
            delay(Duration.of(1, ChronoUnit.MINUTES).seconds)
        }
    }

    private var jobs = listOf<Job>()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getTasks() {
        jobs.forEach { it.cancel() }

        val activeTaskJob = viewModelScope.launch {
            pagePreference.inClinicModeUntil
                .collect { inClinicModeUntil ->
                    val curTime = Instant.now().epochSecond

                    localDateTimeFlow.flatMapConcat {
                        getTodayTasksUseCase.invoke(it)
                    }.collect { taskList ->
                        _todayTasks.value = taskList.filter {
                            if (it.inClinic) inClinicModeUntil > curTime
                            else it.taskResult == null
                        }.sortedByDescending { it.inClinic }
                    }
                }
        }

        val completedTaskJob = viewModelScope.launch {
            pagePreference.inClinicModeUntil
                .collect { inClinicModeUntil ->
                    val curTime = Instant.now().epochSecond

                    getCompletedTasksUseCase(LocalDate.now())
                        .collect {
                            _completedTasks.value = it.filter {
                                !it.inClinic || (inClinicModeUntil > curTime)
                            }
                        }
                }
        }

        jobs = listOf(activeTaskJob, completedTaskJob)
    }
}
