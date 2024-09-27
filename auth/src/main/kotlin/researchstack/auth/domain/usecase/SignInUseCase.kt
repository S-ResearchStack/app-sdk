package researchstack.auth.domain.usecase

import researchstack.auth.data.repository.auth.AuthRepositoryWrapper
import researchstack.auth.domain.model.Authentication
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepositoryWrapper: AuthRepositoryWrapper,
) {
    suspend operator fun invoke(auth: Authentication): Result<Unit> =
        authRepositoryWrapper.signIn(auth)
}
