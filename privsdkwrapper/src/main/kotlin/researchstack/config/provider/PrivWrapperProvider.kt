package researchstack.config.provider

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import researchstack.config.ConfigManager
import researchstack.data.local.file.FileRepository
import researchstack.data.repository.PrivAccMeterRepository
import researchstack.data.repository.PrivBiaRepository
import researchstack.data.repository.PrivEcgRepository
import researchstack.data.repository.PrivHeartRateRepository
import researchstack.data.repository.PrivPpgGreenRepository
import researchstack.data.repository.PrivPpgIrRepository
import researchstack.data.repository.PrivPpgRedRepository
import researchstack.data.repository.PrivSpO2Repository
import researchstack.data.repository.PrivSweatLossRepository
import researchstack.domain.model.priv.Accelerometer
import researchstack.domain.model.priv.Bia
import researchstack.domain.model.priv.EcgSet
import researchstack.domain.model.priv.HeartRate
import researchstack.domain.model.priv.PpgGreen
import researchstack.domain.model.priv.PpgIr
import researchstack.domain.model.priv.PpgRed
import researchstack.domain.model.priv.SpO2
import researchstack.domain.model.priv.SweatLoss
import researchstack.domain.repository.WearableDataRepository
import researchstack.requiredinterface.UserProfileAccessible
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PrivWrapperProvider {
    @Singleton
    @Provides
    fun provideAccelerometerRepository(
        @ApplicationContext context: Context,
        configManager: ConfigManager
    ): WearableDataRepository<Accelerometer> =
        PrivAccMeterRepository(
            FileRepository(Accelerometer::class, context, configManager.dataSplitIntervalInMillis),
        )

    @Singleton
    @Provides
    fun provideBiaRepository(
        userProfileAccessible: UserProfileAccessible,
        @ApplicationContext context: Context,
        configManager: ConfigManager,
    ): WearableDataRepository<Bia> =
        PrivBiaRepository(userProfileAccessible, FileRepository(Bia::class, context, configManager.dataSplitIntervalInMillis))

    @Singleton
    @Provides
    fun provideEcgRepository(
        @ApplicationContext context: Context,
        configManager: ConfigManager,
    ): WearableDataRepository<EcgSet> =
        PrivEcgRepository(FileRepository(EcgSet::class, context, configManager.dataSplitIntervalInMillis))

    @Singleton
    @Provides
    fun providePpgGreenRepository(
        @ApplicationContext context: Context,
        configManager: ConfigManager,
    ): WearableDataRepository<PpgGreen> =
        PrivPpgGreenRepository(FileRepository(PpgGreen::class, context, configManager.dataSplitIntervalInMillis))

    @Singleton
    @Provides
    fun providePpgIrRepository(
        @ApplicationContext context: Context,
        configManager: ConfigManager,
    ): WearableDataRepository<PpgIr> =
        PrivPpgIrRepository(FileRepository(PpgIr::class, context, configManager.dataSplitIntervalInMillis))

    @Singleton
    @Provides
    fun providePpgRedRepository(
        @ApplicationContext context: Context,
        configManager: ConfigManager,
    ): WearableDataRepository<PpgRed> =
        PrivPpgRedRepository(FileRepository(PpgRed::class, context, configManager.dataSplitIntervalInMillis))

    @Singleton
    @Provides
    fun provideSpO2Repository(
        @ApplicationContext context: Context,
        configManager: ConfigManager,
    ): WearableDataRepository<SpO2> =
        PrivSpO2Repository(FileRepository(SpO2::class, context, configManager.dataSplitIntervalInMillis))

    @Singleton
    @Provides
    fun provideSweatLossRepository(
        @ApplicationContext context: Context,
        configManager: ConfigManager,
    ): WearableDataRepository<SweatLoss> =
        PrivSweatLossRepository(FileRepository(SweatLoss::class, context, configManager.dataSplitIntervalInMillis))

    @Singleton
    @Provides
    fun provideHeartRateRepository(
        @ApplicationContext context: Context,
        configManager: ConfigManager,
    ): WearableDataRepository<HeartRate> =
        PrivHeartRateRepository(FileRepository(HeartRate::class, context, configManager.dataSplitIntervalInMillis))
}
