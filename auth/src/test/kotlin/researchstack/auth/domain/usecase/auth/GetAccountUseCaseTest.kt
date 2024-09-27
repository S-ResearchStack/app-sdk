package researchstack.auth.domain.usecase.auth

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import researchstack.auth.NEGATIVE_TEST
import researchstack.auth.domain.repository.AccountRepository
import researchstack.auth.domain.usecase.GetAccountUseCase

internal class GetAccountUseCaseTest {
    private val accountRepository = mockk<AccountRepository>()

    private val getAccountUseCase = GetAccountUseCase(accountRepository)

    @Test
    @Tag(NEGATIVE_TEST)
    fun `should return failure if fail to get account`() = runTest {
        coEvery { accountRepository.getAccount() } returns null

        assertTrue(getAccountUseCase().isFailure)
    }
}
