package researchstack.wearable.standalone.data.repository

import researchstack.domain.model.UserProfile
import researchstack.wearable.standalone.data.local.pref.UserProfilePref
import researchstack.wearable.standalone.domain.repository.UserProfileRepository

class UserProfileRepositoryImpl(private val userProfilePref: UserProfilePref) :
    UserProfileRepository {

    override suspend fun save(userProfile: UserProfile) =
        userProfilePref.save(userProfile)

    override fun getUserProfile() = userProfilePref.getUserProfile()
}
