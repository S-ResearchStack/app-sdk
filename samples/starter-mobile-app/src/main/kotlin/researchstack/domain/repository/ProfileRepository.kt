package researchstack.domain.repository

import researchstack.domain.model.UserProfileModel

interface ProfileRepository {
    suspend fun registerProfile(userProfileModel: UserProfileModel): Result<Unit>
    suspend fun getProfile(): Result<UserProfileModel>
    suspend fun checkRegistered(): Result<Boolean>
    suspend fun updateProfile(userProfileModel: UserProfileModel): Result<Unit>
    suspend fun deregisterProfile(): Result<Unit>
}
