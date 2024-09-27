package researchstack.domain.usecase.task

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.domain.repository.TaskRepository
import java.time.LocalDateTime

internal class FetchTasksUseCaseTest {
    private val taskRepository = mockk<TaskRepository>()

    private val fetchTasksUseCase = FetchTasksUseCase(taskRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return failure if fail to fetch new tasks`() = runTest {
        coEvery { taskRepository.fetchNewTasks(any()) } returns Result.failure(RuntimeException(""))

        assertTrue(
            fetchTasksUseCase(LocalDateTime.now()).isFailure
        )
    }
}
