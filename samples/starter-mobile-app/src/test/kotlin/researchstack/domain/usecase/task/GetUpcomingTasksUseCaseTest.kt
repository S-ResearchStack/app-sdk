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

internal class GetUpcomingTasksUseCaseTest {
    private val taskRepository = mockk<TaskRepository>()

    private val getUpcomingTasksUseCase = GetUpcomingTasksUseCase(taskRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return failure if fail to get upcoming tasks`() = runTest {
        coEvery { taskRepository.getUpcomingTasks(any()) } throws RuntimeException("")

        assertThrows<Exception> {
            getUpcomingTasksUseCase(LocalDateTime.now())
        }
    }
}
