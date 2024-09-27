package researchstack.wearable.standalone.domain.usecase

import kotlinx.coroutines.flow.Flow
import researchstack.domain.model.UserProfile
import researchstack.wearable.standalone.domain.repository.UserProfileRepository
import javax.inject.Inject

class UserProfileUseCase @Inject constructor(
    private val userProfileRepository: UserProfileRepository
) {
    operator fun invoke(): Flow<UserProfile?> {
        return userProfileRepository.getUserProfile()
    }

    suspend operator fun invoke(userProfile: UserProfile) {
        userProfileRepository.save(userProfile)
    }
}
