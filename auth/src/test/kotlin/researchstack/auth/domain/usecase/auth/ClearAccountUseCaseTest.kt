package researchstack.auth.domain.usecase.auth

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import researchstack.auth.NEGATIVE_TEST
import researchstack.auth.domain.repository.AccountRepository
import researchstack.auth.domain.repository.IdTokenRepository
import researchstack.auth.domain.usecase.ClearAccountUseCase

internal class ClearAccountUseCaseTest {
    private val accountRepository = mockk<AccountRepository>()
    private val idTokenRepository = mockk<IdTokenRepository>()

    private val clearAccountUseCase = ClearAccountUseCase(accountRepository, idTokenRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should throw exception if fail to clear account`() = runTest {
        coEvery { accountRepository.clearAccount() } throws RuntimeException()
        coEvery { idTokenRepository.clearIdToken() } throws RuntimeException()

        assertThrows<Exception> {
            clearAccountUseCase()
        }
    }
}
