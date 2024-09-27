package researchstack.config

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import researchstack.BuildConfig
import researchstack.data.local.file.FileRepository
import researchstack.data.local.pref.TrackMeasureTimePref
import researchstack.data.local.pref.UserProfilePref
import researchstack.data.local.pref.WearableMeasurementPref
import researchstack.data.local.room.dao.PassiveDataStatusDao
import researchstack.data.repository.ChannelDataSenderRepository
import researchstack.data.repository.PassiveDataStatusRepositoryImpl
import researchstack.data.repository.SetEcgMeasurementRepositoryImpl
import researchstack.data.repository.TrackMeasureTimeRepositoryImpl
import researchstack.data.repository.UserProfileRepositoryImpl
import researchstack.data.repository.wearevent.WearableEventRepositoryImpl
import researchstack.domain.model.events.WearableBattery
import researchstack.domain.model.events.WearableOffBody
import researchstack.domain.model.events.WearablePowerState
import researchstack.domain.repository.DataSenderRepository
import researchstack.domain.repository.PassiveDataStatusRepository
import researchstack.domain.repository.SetEcgMeasurementRepository
import researchstack.domain.repository.TrackMeasureTimeRepository
import researchstack.domain.repository.UserProfileRepository
import researchstack.domain.repository.WearableEventRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryProvider {
    @Singleton
    @Provides
    fun provideConfigManager(): ConfigManager = ConfigManager(
        dataSplitIntervalInMillis = BuildConfig.DATA_SPLIT_INTERVAL_IN_MILLIS
    )

    @Singleton
    @Provides
    fun provideUserProfileRepository(userProfilePref: UserProfilePref): UserProfileRepository =
        UserProfileRepositoryImpl(userProfilePref)

    @Singleton
    @Provides
    fun provideDataSenderRepository(
        @ApplicationContext context: Context,
    ): DataSenderRepository = ChannelDataSenderRepository(context)

    @Singleton
    @Provides
    fun provideWearableOffBodyRepository(
        @ApplicationContext context: Context,
        configManager: ConfigManager
    ): WearableEventRepository<WearableOffBody> =
        WearableEventRepositoryImpl(
            FileRepository(WearableOffBody::class, context, configManager.dataSplitIntervalInMillis),
        )

    @Singleton
    @Provides
    fun provideWearableBatteryRepository(
        @ApplicationContext context: Context,
        configManager: ConfigManager
    ): WearableEventRepository<WearableBattery> =
        WearableEventRepositoryImpl(
            FileRepository(WearableBattery::class, context, configManager.dataSplitIntervalInMillis),
        )

    @Singleton
    @Provides
    fun provideWearPowerStateEventRepository(
        @ApplicationContext context: Context,
        configManager: ConfigManager
    ): WearableEventRepository<WearablePowerState> =
        WearableEventRepositoryImpl(
            FileRepository(WearablePowerState::class, context, configManager.dataSplitIntervalInMillis),
        )

    @Singleton
    @Provides
    fun provideLastMeasureTimeRepository(trackMeasureTimePref: TrackMeasureTimePref): TrackMeasureTimeRepository =
        TrackMeasureTimeRepositoryImpl(trackMeasureTimePref)

    @Singleton
    @Provides
    fun providePassiveDataStatusRepository(passiveDataStatusDao: PassiveDataStatusDao): PassiveDataStatusRepository =
        PassiveDataStatusRepositoryImpl(passiveDataStatusDao)

    @Singleton
    @Provides
    fun provideSetEcgMeasurementRepository(wearableMeasurementPref: WearableMeasurementPref): SetEcgMeasurementRepository =
        SetEcgMeasurementRepositoryImpl(wearableMeasurementPref)
}
