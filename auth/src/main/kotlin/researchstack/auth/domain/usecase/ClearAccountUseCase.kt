package researchstack.auth.domain.usecase

import researchstack.auth.domain.repository.AccountRepository
import researchstack.auth.domain.repository.IdTokenRepository
import javax.inject.Inject

class ClearAccountUseCase @Inject constructor(
    private val accountPrefRepository: AccountRepository,
    private val idTokenRepository: IdTokenRepository,
) {
    suspend operator fun invoke() {
        accountPrefRepository.clearAccount()
        idTokenRepository.clearIdToken()
    }
}
