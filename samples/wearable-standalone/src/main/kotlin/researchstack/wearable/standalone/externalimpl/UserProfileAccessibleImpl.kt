package researchstack.wearable.standalone.externalimpl

import kotlinx.coroutines.flow.Flow
import researchstack.domain.model.UserProfile
import researchstack.requiredinterface.UserProfileAccessible
import researchstack.wearable.standalone.domain.repository.UserProfileRepository
import javax.inject.Inject

class UserProfileAccessibleImpl @Inject constructor(private val userProfileRepository: UserProfileRepository) :
    UserProfileAccessible {
    override fun getUserProfile(): Flow<UserProfile?> {
        return userProfileRepository.getUserProfile()
    }
}
