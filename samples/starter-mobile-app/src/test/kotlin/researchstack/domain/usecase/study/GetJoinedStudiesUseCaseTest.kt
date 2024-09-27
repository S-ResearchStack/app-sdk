package researchstack.domain.usecase.study

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import researchstack.NEGATIVE_TEST
import researchstack.domain.repository.StudyRepository

internal class GetJoinedStudiesUseCaseTest {
    private val studyRepository = mockk<StudyRepository>()
    private val getJoinedStudiesUseCase = GetJoinedStudiesUseCase(studyRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return failure if `() = runTest {
        coEvery { studyRepository.getJoinedStudies() } throws IllegalStateException()

        assertThrows<Exception> {
            getJoinedStudiesUseCase()
        }
    }
}
