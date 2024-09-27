package researchstack.domain.usecase.study

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.domain.repository.ParticipationRequirementRepository

internal class GetParticipationRequirementUseCaseTest {
    private val participationRequirementRepository = mockk<ParticipationRequirementRepository>()
    private val getParticipationRequirementUseCase =
        GetParticipationRequirementUseCase(participationRequirementRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return failure if fail to get participation-requirement`() = runTest {
        coEvery { participationRequirementRepository.getParticipationRequirement(any()) } returns
            Result.failure(IllegalStateException())

        assertTrue(
            getParticipationRequirementUseCase("studyId").isFailure
        )
    }
}
