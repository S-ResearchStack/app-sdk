package researchstack.domain.usecase.task

import researchstack.domain.repository.TaskRepository
import javax.inject.Inject

class FetchMyTasksUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke() =
        taskRepository.fetchMyTasks()
}
