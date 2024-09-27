package researchstack.wearable.standalone.domain.usecase.auth

import researchstack.auth.data.repository.auth.AuthRepositoryWrapper
import researchstack.auth.domain.model.BasicAuthentication
import researchstack.auth.util.withRetry
import java.util.UUID
import javax.inject.Inject

private const val RETRY_COUNT = 3
private const val RETRY_DELAY = 3000L

class SignUpUseCase @Inject constructor(
    private val authRepositoryWrapper: AuthRepositoryWrapper,
) {
    suspend operator fun invoke(): Result<Unit> {
        return withRetry(RETRY_COUNT, RETRY_DELAY) {
            authRepositoryWrapper.signIn(
                BasicAuthentication("${UUID.randomUUID()}@generated.auto", UUID.randomUUID().toString())
            )
        }
    }
}
