package researchstack.wearable.standalone.domain.repository

import kotlinx.coroutines.flow.Flow
import researchstack.domain.model.UserProfile

interface UserProfileRepository {
    suspend fun save(userProfile: UserProfile)

    fun getUserProfile(): Flow<UserProfile?>
}
