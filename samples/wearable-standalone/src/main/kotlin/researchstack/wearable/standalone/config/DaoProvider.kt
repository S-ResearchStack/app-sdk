package researchstack.wearable.standalone.config

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import researchstack.data.local.room.WearableAppDataBase
import researchstack.data.local.room.dao.PassiveDataStatusDao
import researchstack.wearable.standalone.data.datasource.local.room.ResearchAppDatabase
import researchstack.wearable.standalone.data.datasource.local.room.dao.ShareAgreementDao
import researchstack.wearable.standalone.data.datasource.local.room.dao.StudyDao
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
    fun provideDataShareAgreementDao(@ApplicationContext context: Context): ShareAgreementDao =
        ResearchAppDatabase.getDatabase(context).shareAgreementDao()

    @Singleton
    @Provides
    fun providePassiveDataStatusDao(@ApplicationContext context: Context): PassiveDataStatusDao =
        WearableAppDataBase.getDatabase(context).passiveDataStatusDao()
}
