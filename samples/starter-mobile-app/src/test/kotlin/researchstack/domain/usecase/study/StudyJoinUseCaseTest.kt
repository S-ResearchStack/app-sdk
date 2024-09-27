package researchstack.domain.usecase.study

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.domain.model.eligibilitytest.EligibilityTestResult
import researchstack.domain.model.task.taskresult.SurveyResult
import researchstack.domain.repository.StudyRepository
import java.time.LocalDateTime

internal class StudyJoinUseCaseTest {
    private val studyRepository = mockk<StudyRepository>()
    private val studyJoinUseCase = StudyJoinUseCase(studyRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return failure if fail to join study`() = runTest {
        coEvery { studyRepository.joinStudy(any(), any()) } returns Result.failure(IllegalStateException())

        assertTrue(
            studyJoinUseCase(
                "studyId",
                EligibilityTestResult(
                    "studyid",
                    SurveyResult(1, LocalDateTime.now(), LocalDateTime.now(), emptyList())
                )
            ).isFailure
        )
    }
}
