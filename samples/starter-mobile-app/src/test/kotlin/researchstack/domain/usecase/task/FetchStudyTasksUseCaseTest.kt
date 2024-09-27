package researchstack.domain.usecase.task

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.domain.repository.TaskRepository

internal class FetchStudyTasksUseCaseTest {
    private val taskRepository = mockk<TaskRepository>()

    private val fetchStudyTasksUseCase = FetchStudyTasksUseCase(taskRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return failure if fail to fetch tasks of the study`() = runTest {
        coEvery { taskRepository.fetchTasksOfStudy(any()) } returns Result.failure(RuntimeException(""))

        Assertions.assertTrue(
            fetchStudyTasksUseCase("studyId").isFailure
        )
    }
}
