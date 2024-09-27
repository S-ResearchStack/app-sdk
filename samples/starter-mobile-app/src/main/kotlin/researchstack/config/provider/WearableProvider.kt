package researchstack.config.provider

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import researchstack.BuildConfig
import researchstack.backend.integration.GrpcHealthDataSynchronizer
import researchstack.data.local.room.WearableAppDataBase
import researchstack.data.local.room.dao.PassiveDataStatusDao
import researchstack.data.repository.WatchEventRepositoryImpl
import researchstack.data.repository.wearable.PassiveDataStatusRepositoryImpl
import researchstack.data.repository.wearable.WearableDataReceiverRepositoryImpl
import researchstack.data.repository.wearable.WearableMeasurementPrefRepositoryImpl
import researchstack.data.repository.wearable.WearableMessageSenderRepositoryImpl
import researchstack.data.repository.wearable.WearablePassiveDataStatusSenderRepositoryImpl
import researchstack.domain.model.events.MobileWearConnection
import researchstack.domain.model.shealth.HealthDataModel
import researchstack.domain.repository.PassiveDataStatusRepository
import researchstack.domain.repository.ShareAgreementRepository
import researchstack.domain.repository.StudyRepository
import researchstack.domain.repository.WatchEventsRepository
import researchstack.domain.repository.WearableDataReceiverRepository
import researchstack.domain.repository.WearableMeasurementPrefRepository
import researchstack.domain.repository.WearableMessageSenderRepository
import researchstack.domain.repository.WearablePassiveDataStatusSenderRepository
import javax.inject.Singleton
import researchstack.data.local.file.FileRepository as CommonFileRepository

@Module
@InstallIn(SingletonComponent::class)
object WearableProvider {
    @Singleton
    @Provides
    fun provideWearableMessageSenderRepository(@ApplicationContext context: Context): WearableMessageSenderRepository =
        WearableMessageSenderRepositoryImpl(context)

    @Singleton
    @Provides
    fun provideWearableMeasurementPrefRepository(@ApplicationContext context: Context): WearableMeasurementPrefRepository =
        WearableMeasurementPrefRepositoryImpl(context)

    @Singleton
    @Provides
    fun provideWearablePassiveDataStatusSenderRepository(
        @ApplicationContext context: Context
    ): WearablePassiveDataStatusSenderRepository =
        WearablePassiveDataStatusSenderRepositoryImpl(context)

    @Singleton
    @Provides
    fun providePassiveDataStatusRepository(passiveDataStatusDao: PassiveDataStatusDao): PassiveDataStatusRepository =
        PassiveDataStatusRepositoryImpl(
            passiveDataStatusDao
        )

    @Singleton
    @Provides
    fun provideWatchConnectedEventFileRepository(@ApplicationContext context: Context): WatchEventsRepository<MobileWearConnection> =
        WatchEventRepositoryImpl(
            CommonFileRepository(
                MobileWearConnection::class,
                context,
                BuildConfig.DATA_SPLIT_INTERVAL_IN_MILLIS
            )
        )

    @Singleton
    @Provides
    fun provideWearableDBRepository(
        studyRepository: StudyRepository,
        shareAgreementRepository: ShareAgreementRepository,
        wearableAppDatabase: WearableAppDataBase,
        grpcHealthDataSynchronizer: GrpcHealthDataSynchronizer<HealthDataModel>,
    ): WearableDataReceiverRepository =
        WearableDataReceiverRepositoryImpl(
            studyRepository,
            shareAgreementRepository,
            wearableAppDatabase,
            grpcHealthDataSynchronizer
        )
}
