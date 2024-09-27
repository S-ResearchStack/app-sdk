package researchstack.config.provider

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import researchstack.data.datasource.local.room.ResearchAppDatabase
import researchstack.data.datasource.local.room.dao.AccelerometerDao
import researchstack.data.datasource.local.room.dao.EventDao
import researchstack.data.datasource.local.room.dao.FileUploadRequestDao
import researchstack.data.datasource.local.room.dao.LightDao
import researchstack.data.datasource.local.room.dao.LogDao
import researchstack.data.datasource.local.room.dao.ParticipationRequirementDao
import researchstack.data.datasource.local.room.dao.ShareAgreementDao
import researchstack.data.datasource.local.room.dao.SpeedDao
import researchstack.data.datasource.local.room.dao.StudyDao
import researchstack.data.datasource.local.room.dao.TaskDao
import researchstack.data.local.room.WearableAppDataBase
import researchstack.data.local.room.dao.PassiveDataStatusDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoProvider {
    @Singleton
    @Provides
    fun provideStudyDao(@ApplicationContext context: Context): StudyDao =
        ResearchAppDatabase.getDatabase(context).studyDao()

    @Singleton
    @Provides
    fun provideParticipationRequirementDao(
        @ApplicationContext context: Context,
    ): ParticipationRequirementDao =
        ResearchAppDatabase.getDatabase(context).participationRequirementDao()

    @Singleton
    @Provides
    fun provideTaskDao(@ApplicationContext context: Context): TaskDao =
        ResearchAppDatabase.getDatabase(context).taskDao()

    @Singleton
    @Provides
    fun provideDataShareAgreementDao(@ApplicationContext context: Context): ShareAgreementDao =
        ResearchAppDatabase.getDatabase(context).shareAgreementDao()

    @Singleton
    @Provides
    fun provideLightDao(@ApplicationContext context: Context): LightDao =
        ResearchAppDatabase.getDatabase(context).lightDao()

    @Singleton
    @Provides
    fun provideAccelerometerDao(@ApplicationContext context: Context): AccelerometerDao =
        ResearchAppDatabase.getDatabase(context).accelerometerDao()

    @Singleton
    @Provides
    fun provideSpeedDao(@ApplicationContext context: Context): SpeedDao =
        ResearchAppDatabase.getDatabase(context).speedDao()

    @Singleton
    @Provides
    fun provideLogDao(@ApplicationContext context: Context): LogDao =
        ResearchAppDatabase.getDatabase(context).logDao()

    @Singleton
    @Provides
    fun provideFileUploadRequestDao(@ApplicationContext context: Context): FileUploadRequestDao =
        ResearchAppDatabase.getDatabase(context).fileUploadRequestDao()

    @Singleton
    @Provides
    fun provideEventDao(@ApplicationContext context: Context): EventDao =
        ResearchAppDatabase.getDatabase(context).eventDao()

    @Singleton
    @Provides
    fun provideResearchAppDatabase(@ApplicationContext context: Context): ResearchAppDatabase =
        ResearchAppDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun provideWearableAppDatabase(@ApplicationContext context: Context): WearableAppDataBase =
        WearableAppDataBase.getDatabase(context)

    @Singleton
    @Provides
    fun provideWearablePassiveDataStatusDao(
        @ApplicationContext context: Context
    ): PassiveDataStatusDao = WearableAppDataBase.getDatabase(context).passiveDataStatusDao()
}
