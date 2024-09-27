package researchstack.wearable.standalone.config

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import researchstack.requiredinterface.UserProfileAccessible
import researchstack.wearable.standalone.domain.repository.UserProfileRepository
import researchstack.wearable.standalone.externalimpl.UserProfileAccessibleImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ExternalInterfaceProvider {
    @Singleton
    @Provides
    fun provideUserProfileAccessible(
        userProfileRepository: UserProfileRepository,
    ): UserProfileAccessible = UserProfileAccessibleImpl(userProfileRepository)
}
