package researchstack.domain.usecase.profile

import researchstack.domain.model.UserProfileModel
import researchstack.domain.repository.ProfileRepository
import javax.inject.Inject

class RegisterProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(userProfileModel: UserProfileModel): Result<Unit> {
        return profileRepository.registerProfile(userProfileModel)
    }
}
