package researchstack.domain.usecase.profile

import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.domain.model.UserProfileModel
import researchstack.domain.repository.ProfileRepository
import java.time.LocalDate

internal class RegisterProfileUseCaseTest {
    private val profileRepository = mockk<ProfileRepository>()

    private val registerProfileUseCase = RegisterProfileUseCase(profileRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return failure if fail to register profile`() = runTest {
        val profileSlot = slot<UserProfileModel>()
        coEvery { profileRepository.registerProfile(capture(profileSlot)) } returns Result.failure(RuntimeException())

        val userProfile = UserProfileModel(
            firstName = "john",
            lastName = "doe",
            birthday = LocalDate.now(),
            email = "",
            phoneNumber = "",
            address = "",
        )
        Assertions.assertTrue(
            registerProfileUseCase(userProfile).isFailure
        )

        assertEquals(userProfile.firstName, profileSlot.captured.firstName)
        assertEquals(userProfile.lastName, profileSlot.captured.lastName)
        assertEquals(userProfile.birthday, profileSlot.captured.birthday)
        assertEquals(userProfile.email, profileSlot.captured.email)
        assertEquals(userProfile.phoneNumber, profileSlot.captured.phoneNumber)
        assertEquals(userProfile.address, profileSlot.captured.address)
    }
}
