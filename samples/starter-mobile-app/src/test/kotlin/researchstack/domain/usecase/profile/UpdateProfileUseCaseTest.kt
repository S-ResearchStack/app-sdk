package researchstack.domain.usecase.profile

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.domain.model.UserProfileModel
import researchstack.domain.repository.ProfileRepository
import java.time.LocalDate

internal class UpdateProfileUseCaseTest {

    private val profileRepository = mockk<ProfileRepository>()
    private val updateProfileUseCase = UpdateProfileUseCase(profileRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return failure if fail to update profile`() = runTest {
        coEvery { profileRepository.updateProfile(any()) } returns Result.failure(RuntimeException())

        assertTrue(
            updateProfileUseCase(
                UserProfileModel(
                    firstName = "john",
                    lastName = "doe",
                    birthday = LocalDate.now(),
                    email = "",
                    phoneNumber = "",
                    address = "",
                )
            ).isFailure
        )
    }
}
