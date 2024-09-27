package researchstack.auth.domain.usecase

import researchstack.auth.domain.model.Account
import researchstack.auth.domain.repository.AccountRepository
import javax.inject.Inject

class GetAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
) {

    suspend operator fun invoke(): Result<Account> =
        accountRepository.getAccount()
            ?.let { Result.success(it) }
            ?: Result.failure(IllegalStateException("no account"))
}
