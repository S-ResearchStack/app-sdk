package researchstack.wearable.standalone.domain.repository

import researchstack.wearable.standalone.domain.model.UserProfileModel

interface ProfileRepository {
    suspend fun registerProfile(userProfileModel: UserProfileModel): Result<Unit>
}
