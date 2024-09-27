package researchstack.domain.usecase.task

import researchstack.domain.repository.TaskRepository
import java.time.LocalDateTime
import javax.inject.Inject

class FetchTasksUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(lastSyncTime: LocalDateTime) =
        taskRepository.fetchNewTasks(lastSyncTime)
}
