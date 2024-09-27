package researchstack.requiredinterface

import kotlinx.coroutines.flow.Flow
import researchstack.domain.model.UserProfile

interface UserProfileAccessible {
    fun getUserProfile(): Flow<UserProfile?>
}
