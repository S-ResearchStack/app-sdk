package researchstack.domain.usecase.task

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import researchstack.NEGATIVE_TEST
import researchstack.domain.repository.TaskRepository
import java.time.LocalDate

internal class GetCompletedTasksUseCaseTest {
    private val taskRepository = mockk<TaskRepository>()

    private val getCompletedTasksUseCase = GetCompletedTasksUseCase(taskRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return failure if fail to get completed tasks`() = runTest {
        coEvery { taskRepository.getCompletedTasks(any()) } throws RuntimeException("")

        assertThrows<Exception> {
            getCompletedTasksUseCase(LocalDate.now())
        }
    }
}
