package researchstack.auth.domain.usecase.auth

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.auth.NEGATIVE_TEST
import researchstack.auth.data.repository.auth.AuthRepositoryWrapper
import researchstack.auth.domain.model.Account
import researchstack.auth.domain.model.IdToken
import researchstack.auth.domain.usecase.CheckSignInUseCase

internal class CheckSignInUseCaseTest {

    private val authRepositoryWrapper = mockk<AuthRepositoryWrapper>()

    private val checkSignInUseCase = CheckSignInUseCase(authRepositoryWrapper)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return failure if no account`() = runTest {
        coEvery { authRepositoryWrapper.getAccount() } returns
            Result.failure(IllegalStateException("no account info. Sign in first"))
        assertTrue(
            checkSignInUseCase().isFailure
        )
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return failure if fail to get id-token`() = runTest {
        coEvery { authRepositoryWrapper.getAccount() } returns Result.success(Account("account-id", "provider"))
        coEvery { authRepositoryWrapper.getIdToken() } returns Result.failure(RuntimeException())
        assertTrue(
            checkSignInUseCase().isFailure
        )
    }

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return success with false if subject-id is not matched`() = runTest {
        coEvery { authRepositoryWrapper.getAccount() } returns Result.success(Account("account-id", "provider"))

        val idToken = mockk<IdToken> {
            every { subject } returns "not-matched-subject-id"
        }
        coEvery { authRepositoryWrapper.getIdToken() } returns Result.success(idToken)

        val result = checkSignInUseCase()
        assertTrue(result.isSuccess)
        assertFalse(result.getOrThrow())
    }
}
