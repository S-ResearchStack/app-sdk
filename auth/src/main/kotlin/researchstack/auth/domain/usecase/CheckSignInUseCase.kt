package researchstack.auth.domain.usecase

import researchstack.auth.data.repository.auth.AuthRepositoryWrapper
import researchstack.auth.util.withRetry
import javax.inject.Inject

private const val RETRY_COUNT = 3
private const val RETRY_DELAY = 3000L

class CheckSignInUseCase @Inject constructor(
    private val authRepositoryWrapper: AuthRepositoryWrapper,
) {
    suspend operator fun invoke(): Result<Boolean> {
        val account = authRepositoryWrapper.getAccount().getOrNull() ?: return Result.failure(IllegalStateException("no account"))
        return withRetry(RETRY_COUNT, RETRY_DELAY) {
            authRepositoryWrapper.getIdToken()
        }.map {
            account.id == it.subject
        }
    }
}
