package researchstack.domain.usecase.task

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import researchstack.NEGATIVE_TEST
import researchstack.domain.repository.TaskRepository
import java.time.LocalDateTime

internal class GetActiveTasksUseCaseTest {
    private val taskRepository = mockk<TaskRepository>()

    private val getActiveTasksUseCase = GetActiveTasksUseCase(taskRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw if taskRepository throws exception`() = runTest {
        coEvery { taskRepository.getActiveTasks(any()) } throws RuntimeException("")

        assertThrows<Exception> {
            getActiveTasksUseCase(LocalDateTime.now())
        }
    }
}
