package researchstack.domain.usecase.task

import researchstack.domain.repository.TaskRepository
import javax.inject.Inject

class FetchStudyTasksUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(studyId: String) =
        taskRepository.fetchTasksOfStudy(studyId)
}
