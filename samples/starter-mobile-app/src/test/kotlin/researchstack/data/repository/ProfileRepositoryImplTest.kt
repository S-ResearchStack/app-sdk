package researchstack.data.repository

import io.grpc.Status
import io.grpc.StatusException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.NEGATIVE_TEST
import researchstack.POSITIVE_TEST
import researchstack.backend.integration.outport.SubjectOutPort
import researchstack.data.datasource.grpc.mapper.toData
import researchstack.domain.exception.AlreadyExistedUserException
import researchstack.domain.model.UserProfileModel
import java.time.LocalDate

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
internal class ProfileRepositoryImplTest {

    private val serviceMock = mockk<SubjectOutPort>()
    private val profileRepository = ProfileRepositoryImpl(serviceMock)
    private val testUserProfile = UserProfileModel(
        firstName = "udit",
        lastName = "jain",
        birthday = LocalDate.now(),
        email = "udit.jain@research-service.io",
        address = "secret",
        phoneNumber = "secret"
    )

    @Test
    @Tag(POSITIVE_TEST)
    fun `registerProfile should return success`() = runTest {
        coEvery { serviceMock.registerSubject(any()) } returns Result.success(Unit)
        assertTrue(
            profileRepository.registerProfile(testUserProfile).isSuccess
        )
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `registerProfile should return failure with ALREADY_EXISTS error if already registered user`() =
        runTest {
            coEvery { serviceMock.registerSubject(any()) } returns Result.failure(Status.ALREADY_EXISTS.asException())
            val result = profileRepository.registerProfile(testUserProfile)
            assertTrue(result.isFailure)
            assertEquals(AlreadyExistedUserException, result.exceptionOrNull())
        }

    @Test
    @Tag(POSITIVE_TEST)
    fun `getProfile should return success with Profile`() = runTest {
        coEvery { serviceMock.getSubjectProfile() } returns Result.success(testUserProfile.toData())

        val result = profileRepository.getProfile()

        assertTrue(result.isSuccess)
        assertEquals(testUserProfile, result.getOrThrow())
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `getProfile should return failure with if not registered user`() = runTest {
        coEvery { serviceMock.getSubjectProfile() } returns Result.failure(Status.NOT_FOUND.asException())
        val result = profileRepository.getProfile()

        assertTrue(result.isFailure)
        // FIXME should convert to domain exception
        assertEquals(Status.NOT_FOUND, (result.exceptionOrNull() as? StatusException)?.status)
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `checkRegistered should return true`() = runTest {
        coEvery { serviceMock.getSubjectProfile() } returns Result.success(testUserProfile.toData())

        val result = profileRepository.checkRegistered()

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow())
    }

    @Test
    @Tag(POSITIVE_TEST)
    fun `checkRegistered should return false if fail to get profile`() = runTest {
        coEvery { serviceMock.getSubjectProfile() } returns Result.failure(Status.NOT_FOUND.asException())

        val result = profileRepository.checkRegistered()

        assertTrue(result.isSuccess)
        assertFalse(result.getOrThrow())
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `checkRegistered should return failure with UNAUTHENTICATED if request is unauthenticated`() =
        runTest {
            coEvery { serviceMock.getSubjectProfile() } returns Result.failure(Status.UNAUTHENTICATED.asException())

            val result = profileRepository.checkRegistered()

            assertTrue(result.isFailure)
            // FIXME should convert to domain exception
            assertEquals(
                Status.UNAUTHENTICATED,
                (result.exceptionOrNull() as? StatusException)?.status
            )
        }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `updateProfile should return failure with UNAUTHENTICATED if request is unauthenticated`() =
        runTest {
            coEvery { serviceMock.updateSubjectProfile(any()) } returns Result.failure(Status.UNAUTHENTICATED.asException())

            val result = profileRepository.updateProfile(testUserProfile)

            assertTrue(result.isFailure)
            // FIXME should convert to domain exception
            assertEquals(
                Status.UNAUTHENTICATED,
                (result.exceptionOrNull() as? StatusException)?.status
            )
        }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `deregisterProfile should return failure with UNAUTHENTICATED if request is unauthenticated`() =
        runTest {
            coEvery { serviceMock.deregisterSubject() } returns Result.failure(Status.UNAUTHENTICATED.asException())

            val result = profileRepository.deregisterProfile()

            assertTrue(result.isFailure)
            // FIXME should convert to domain exception
            assertEquals(
                Status.UNAUTHENTICATED,
                (result.exceptionOrNull() as? StatusException)?.status
            )
        }
}
