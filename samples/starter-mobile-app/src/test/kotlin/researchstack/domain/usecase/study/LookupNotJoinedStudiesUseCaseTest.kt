package researchstack.domain.usecase.study

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import researchstack.NEGATIVE_TEST
import researchstack.domain.repository.StudyRepository

internal class LookupNotJoinedStudiesUseCaseTest {
    private val studyRepository = mockk<StudyRepository>()
    private val lookupNotJoinedStudiesUseCase = LookupNotJoinedStudiesUseCase(studyRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return failure if `() = runTest {
        coEvery { studyRepository.getNotJoinedStudies() } throws IllegalStateException()

        assertThrows<Exception> {
            lookupNotJoinedStudiesUseCase()
        }
    }
}
