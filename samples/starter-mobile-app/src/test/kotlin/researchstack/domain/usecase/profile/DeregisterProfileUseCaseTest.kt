package researchstack.domain.usecase.profile

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.domain.repository.ProfileRepository

internal class DeregisterProfileUseCaseTest {
    private val profileRepository = mockk<ProfileRepository>()

    private val deregisterProfileUseCase = DeregisterProfileUseCase(profileRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return failure if fail to deregister profile`() = runTest {
        coEvery { profileRepository.deregisterProfile() } returns Result.failure(RuntimeException())

        assertTrue(
            deregisterProfileUseCase().isFailure
        )
    }
}
