package researchstack.wearable.standalone.domain.usecase.profile

import researchstack.wearable.standalone.domain.model.UserProfileModel
import researchstack.wearable.standalone.domain.repository.ProfileRepository
import javax.inject.Inject

class RegisterProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(userProfileModel: UserProfileModel): Result<Unit> {
        return profileRepository.registerProfile(userProfileModel)
    }
}
