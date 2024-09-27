package researchstack.wearable.standalone.config

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import researchstack.config.ConfigManager
import researchstack.data.local.room.dao.PassiveDataStatusDao
import researchstack.wearable.standalone.data.local.pref.TrackMeasureTimePref
import researchstack.wearable.standalone.data.local.pref.UserProfilePref
import researchstack.wearable.standalone.data.repository.PassiveDataStatusRepositoryImpl
import researchstack.wearable.standalone.data.repository.TrackMeasureTimeRepositoryImpl
import researchstack.wearable.standalone.data.repository.UserProfileRepositoryImpl
import researchstack.wearable.standalone.domain.repository.PassiveDataStatusRepository
import researchstack.wearable.standalone.domain.repository.TrackMeasureTimeRepository
import researchstack.wearable.standalone.domain.repository.UserProfileRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryProvider {
    @Singleton
    @Provides
    fun provideConfigManager(): ConfigManager = ConfigManager()

    @Singleton
    @Provides
    fun provideUserProfileRepository(userProfilePref: UserProfilePref): UserProfileRepository =
        UserProfileRepositoryImpl(userProfilePref)

    @Singleton
    @Provides
    fun provideLastMeasureTimeRepository(trackMeasureTimePref: TrackMeasureTimePref): TrackMeasureTimeRepository =
        TrackMeasureTimeRepositoryImpl(trackMeasureTimePref)

    @Singleton
    @Provides
    fun providePassiveDataStatusRepository(passiveDataStatusDao: PassiveDataStatusDao): PassiveDataStatusRepository =
        PassiveDataStatusRepositoryImpl(passiveDataStatusDao)
}
