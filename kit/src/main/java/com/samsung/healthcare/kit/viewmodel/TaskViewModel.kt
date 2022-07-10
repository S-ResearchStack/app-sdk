package com.samsung.healthcare.kit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samsung.healthcare.kit.repository.TaskRepository
import com.samsung.healthcare.kit.task.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class TaskViewModel(private val taskRepository: TaskRepository, private var date: LocalDate = LocalDate.now()) :
    ViewModel() {
    private val _upcomingTasks = MutableStateFlow(TasksState(emptyList()))
    val upcomingTasks: StateFlow<TasksState> = _upcomingTasks

    private val _completedTasks = MutableStateFlow(TasksState(emptyList()))
    val completedTasks: StateFlow<TasksState> = _completedTasks

    init {
        viewModelScope.launch {
            updateDailyTasks()
        }
    }

    fun done(task: Task) {
        viewModelScope.launch {
            taskRepository.done(task)
            updateDailyTasks()
        }
    }

    fun setDate(date: LocalDate) {
        // TODO apply state flow when date is changed
        this.date = date
        viewModelScope.launch {
            updateDailyTasks()
        }
    }

    private suspend fun updateDailyTasks() {
        taskRepository.getUpcomingDailyTasks(date)
            .collect { tasks ->
                _upcomingTasks.value = TasksState(tasks)
            }

        taskRepository.getCompletedDailyTasks(date)
            .collect { tasks ->
                _completedTasks.value = TasksState(tasks)
            }
    }

    data class TasksState(val tasks: List<Task>)
}
