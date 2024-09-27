package researchstack.domain.usecase.profile

import researchstack.domain.repository.ProfileRepository
import javax.inject.Inject

class DeregisterProfileUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(): Result<Unit> = profileRepository.deregisterProfile()
}
