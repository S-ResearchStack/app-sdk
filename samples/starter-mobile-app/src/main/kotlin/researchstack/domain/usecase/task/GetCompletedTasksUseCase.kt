package researchstack.domain.usecase.task

import researchstack.domain.repository.TaskRepository
import java.time.LocalDate
import javax.inject.Inject

class GetCompletedTasksUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    operator fun invoke(targetDate: LocalDate) =
        taskRepository.getCompletedTasks(targetDate)
}
