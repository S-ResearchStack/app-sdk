package researchstack.domain.usecase.task

import researchstack.domain.repository.TaskRepository
import javax.inject.Inject

class RemoveStudyTasksUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(studyId: String) = taskRepository.removeStudyTasks(studyId)
}
