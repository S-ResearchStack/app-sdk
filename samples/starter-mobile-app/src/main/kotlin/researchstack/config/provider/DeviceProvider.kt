package researchstack.config.provider

import android.content.Context
import android.hardware.SensorManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import researchstack.backend.integration.GrpcHealthDataSynchronizer
import researchstack.data.datasource.local.pref.dataStore
import researchstack.data.datasource.local.room.dao.AccelerometerDao
import researchstack.data.datasource.local.room.dao.LightDao
import researchstack.data.datasource.local.room.dao.ShareAgreementDao
import researchstack.data.datasource.local.room.dao.SpeedDao
import researchstack.data.datasource.local.room.dao.StudyDao
import researchstack.data.repository.sensor.AcceleroRepositoryImpl
import researchstack.data.repository.sensor.LightRepositoryImpl
import researchstack.data.repository.sensor.PermittedSensorTypeRepoImpl
import researchstack.data.repository.sensor.SpeedRepositoryImpl
import researchstack.domain.model.shealth.HealthDataModel
import researchstack.domain.repository.sensor.AcceleroRepository
import researchstack.domain.repository.sensor.LightRepository
import researchstack.domain.repository.sensor.PermittedSensorTypeRepository
import researchstack.domain.repository.sensor.SpeedRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DeviceProvider {
    @Singleton
    @Provides
    fun provideSensorManager(@ApplicationContext context: Context): SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    @Singleton
    @Provides
    fun provideLightRepository(
        @ApplicationContext context: Context,
        sensorManager: SensorManager,
        lightDao: LightDao,
        grpcHealthDataSynchronizer: GrpcHealthDataSynchronizer<HealthDataModel>,
    ): LightRepository =
        LightRepositoryImpl(sensorManager, lightDao, grpcHealthDataSynchronizer, context.dataStore)

    @Singleton
    @Provides
    fun provideSpeedRepository(
        @ApplicationContext context: Context,
        speedDao: SpeedDao,
        grpcHealthDataSynchronizer: GrpcHealthDataSynchronizer<HealthDataModel>,
    ): SpeedRepository =
        SpeedRepositoryImpl(context, speedDao, grpcHealthDataSynchronizer, context.dataStore)

    @Singleton
    @Provides
    fun provideAcceleroRepository(
        @ApplicationContext context: Context,
        sensorManager: SensorManager,
        accelerometerDao: AccelerometerDao,
        grpcHealthDataSynchronizer: GrpcHealthDataSynchronizer<HealthDataModel>,
    ): AcceleroRepository =
        AcceleroRepositoryImpl(
            sensorManager,
            accelerometerDao,
            grpcHealthDataSynchronizer,
            context.dataStore
        )

    @Singleton
    @Provides
    fun providePermittedSensorTypeRepository(
        studyDao: StudyDao,
        shareAgreementDao: ShareAgreementDao,
    ): PermittedSensorTypeRepository =
        PermittedSensorTypeRepoImpl(studyDao, shareAgreementDao)
}
