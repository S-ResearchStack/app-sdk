package researchstack.auth.domain.usecase

import researchstack.auth.domain.repository.IdTokenRepository
import javax.inject.Inject

class ClearIdTokenUseCase @Inject constructor(private val idTokenRepository: IdTokenRepository) {
    suspend operator fun invoke() = idTokenRepository.clearIdToken()
}
