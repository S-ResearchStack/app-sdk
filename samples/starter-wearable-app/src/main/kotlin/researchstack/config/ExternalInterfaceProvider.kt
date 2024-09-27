package researchstack.config

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import researchstack.domain.repository.UserProfileRepository
import researchstack.externalimpl.UserProfileAccessibleImpl
import researchstack.requiredinterface.UserProfileAccessible
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
