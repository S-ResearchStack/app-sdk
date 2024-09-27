package researchstack.domain.usecase.study

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.domain.exception.NotFoundStudyException
import researchstack.domain.repository.StudyRepository

internal class GetCloseStudyUseCaseTest {
    private val studyRepository = mockk<StudyRepository>()
    private val getCloseStudyUseCase = GetCloseStudyUseCase(studyRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return failure when not-existed study id`() = runTest {
        coEvery { studyRepository.getStudyByParticipationCode(any()) } returns Result.failure(NotFoundStudyException)

        assertTrue(
            getCloseStudyUseCase("not-existed-study").isFailure
        )
    }
}
