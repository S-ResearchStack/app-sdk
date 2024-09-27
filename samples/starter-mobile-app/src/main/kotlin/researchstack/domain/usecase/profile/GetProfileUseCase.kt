package researchstack.domain.usecase.profile

import researchstack.domain.model.UserProfileModel
import researchstack.domain.repository.ProfileRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(): Result<UserProfileModel> = profileRepository.getProfile()
}
