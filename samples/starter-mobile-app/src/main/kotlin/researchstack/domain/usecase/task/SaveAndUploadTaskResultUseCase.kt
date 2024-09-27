package researchstack.domain.usecase.task

import researchstack.domain.model.task.taskresult.TaskResult
import researchstack.domain.repository.TaskRepository
import javax.inject.Inject

class SaveAndUploadTaskResultUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskResult: TaskResult) = runCatching {
        taskRepository.saveResult(taskResult).getOrThrow()
        taskRepository.uploadTaskResult(taskResult.id).getOrThrow()
    }
}
