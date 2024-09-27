package researchstack.domain.usecase.task

import researchstack.domain.repository.TaskRepository
import java.time.LocalDateTime
import javax.inject.Inject

class GetActiveTasksUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    operator fun invoke(targetDateTime: LocalDateTime) =
        taskRepository.getActiveTasks(targetDateTime)
}
