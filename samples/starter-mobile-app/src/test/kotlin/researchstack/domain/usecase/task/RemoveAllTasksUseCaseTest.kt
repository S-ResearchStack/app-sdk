package researchstack.domain.usecase.task

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.domain.repository.TaskRepository

internal class RemoveAllTasksUseCaseTest {
    private val taskRepository = mockk<TaskRepository>()

    private val removeAllTasksUseCase = RemoveAllTasksUseCase(taskRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return failure if fail to remove tasks`() = runTest {
        coEvery { taskRepository.removeAllTasks() } returns Result.failure(RuntimeException(""))

        Assertions.assertTrue(
            removeAllTasksUseCase().isFailure
        )
    }
}
