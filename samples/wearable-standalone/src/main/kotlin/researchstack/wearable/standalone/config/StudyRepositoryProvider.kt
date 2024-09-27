package researchstack.wearable.standalone.config

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import researchstack.auth.data.datasource.local.pref.dataStore
import researchstack.wearable.standalone.data.datasource.grpc.GrpcStudyDataSource
import researchstack.wearable.standalone.data.datasource.local.room.dao.ShareAgreementDao
import researchstack.wearable.standalone.data.local.pref.SubjectIdPref
import researchstack.wearable.standalone.data.repository.study.ShareAgreementRepositoryImpl
import researchstack.wearable.standalone.data.repository.study.StudyRepositoryImpl
import researchstack.wearable.standalone.domain.repository.ShareAgreementRepository
import researchstack.wearable.standalone.domain.repository.StudyRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StudyRepositoryProvider {
    @Singleton
    @Provides
    fun provideStudyRepository(
        @ApplicationContext
        context: Context,
        grpcStudyDataSource: GrpcStudyDataSource,
    ): StudyRepository =
        StudyRepositoryImpl(
            SubjectIdPref(context.dataStore),
            grpcStudyDataSource
        )

    @Singleton
    @Provides
    fun provideShareAgreementRepository(dataShareAgreementDao: ShareAgreementDao): ShareAgreementRepository =
        ShareAgreementRepositoryImpl(dataShareAgreementDao)
}
