package researchstack.externalimpl

import kotlinx.coroutines.flow.Flow
import researchstack.domain.model.UserProfile
import researchstack.domain.repository.UserProfileRepository
import researchstack.requiredinterface.UserProfileAccessible
import javax.inject.Inject

class UserProfileAccessibleImpl @Inject constructor(private val userProfileRepository: UserProfileRepository) :
    UserProfileAccessible {
    override fun getUserProfile(): Flow<UserProfile?> {
        return userProfileRepository.getUserProfile()
    }
}
