package researchstack.domain.usecase.task

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.domain.model.task.ActivityType
import researchstack.domain.model.task.taskresult.ActivityResult
import researchstack.domain.repository.TaskRepository
import java.time.LocalDateTime

internal class SaveAndUploadTaskResultUseCaseTest {
    private val taskRepository = mockk<TaskRepository>()
    private val saveAndUploadTaskResultUseCase = SaveAndUploadTaskResultUseCase(taskRepository)

    @Test
    @Tag(POSITIVE_TEST)
    fun `should return success`() = runTest {
        coEvery { taskRepository.saveResult(any()) } returns Result.success(Unit)
        coEvery { taskRepository.uploadTaskResult(any()) } returns Result.success(Unit)

        assertTrue(
            saveAndUploadTaskResultUseCase(
                ActivityResult(
                    1,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    ActivityType.TAPPING_SPEED,
                    mapOf(),
                )
            ).isSuccess
        )
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return failure if fail to save result`() = runTest {
        coEvery { taskRepository.saveResult(any()) } returns Result.failure(RuntimeException())

        assertTrue(
            saveAndUploadTaskResultUseCase(
                ActivityResult(
                    1,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    ActivityType.TAPPING_SPEED,
                    mapOf(),
                )
            ).isFailure
        )
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return failure if fail to upload result`() = runTest {
        coEvery { taskRepository.saveResult(any()) } returns Result.success(Unit)
        coEvery { taskRepository.uploadTaskResult(any()) } returns Result.failure(RuntimeException())

        assertTrue(
            saveAndUploadTaskResultUseCase(
                ActivityResult(
                    1,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    ActivityType.TAPPING_SPEED,
                    mapOf(),
                )
            ).isFailure
        )
    }
}
