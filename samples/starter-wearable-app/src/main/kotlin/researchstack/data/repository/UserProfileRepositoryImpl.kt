package researchstack.data.repository

import researchstack.data.local.pref.UserProfilePref
import researchstack.domain.model.UserProfile
import researchstack.domain.repository.UserProfileRepository

class UserProfileRepositoryImpl(private val userProfilePref: UserProfilePref) :
    UserProfileRepository {

    override suspend fun save(userProfile: UserProfile) =
        userProfilePref.save(userProfile)

    override fun getUserProfile() = userProfilePref.getUserProfile()
}
