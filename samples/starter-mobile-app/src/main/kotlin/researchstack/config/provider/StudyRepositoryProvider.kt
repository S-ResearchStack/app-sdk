package researchstack.config.provider

import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import researchstack.data.datasource.grpc.GrpcStudyDataSource
import researchstack.data.datasource.grpc.GrpcTaskDataSource
import researchstack.data.datasource.local.room.dao.ParticipationRequirementDao
import researchstack.data.datasource.local.room.dao.ShareAgreementDao
import researchstack.data.datasource.local.room.dao.StudyDao
import researchstack.data.datasource.local.room.dao.TaskDao
import researchstack.data.repository.TaskRepositoryImpl
import researchstack.data.repository.study.ParticipationRequirementRepositoryImpl
import researchstack.data.repository.study.ShareAgreementRepositoryImpl
import researchstack.data.repository.study.StudyRepositoryImpl
import researchstack.domain.repository.ParticipationRequirementRepository
import researchstack.domain.repository.ShareAgreementRepository
import researchstack.domain.repository.StudyRepository
import researchstack.domain.repository.TaskRepository
import researchstack.domain.repository.UserStatusRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StudyRepositoryProvider {
    @Singleton
    @Provides
    fun provideStudyRepository(
        studyDao: StudyDao,
        grpcStudyDataSource: GrpcStudyDataSource,
        userStatusRepository: UserStatusRepository
    ): StudyRepository =
        StudyRepositoryImpl(
            studyDao,
            grpcStudyDataSource,
            userStatusRepository
        )

    @Singleton
    @Provides
    fun provideParticipationRequirementRepository(
        participationRequirementDao: ParticipationRequirementDao,
        grpcStudyDataSource: GrpcStudyDataSource,
    ): ParticipationRequirementRepository =
        ParticipationRequirementRepositoryImpl(
            participationRequirementDao,
            grpcStudyDataSource
        )

    @Singleton
    @Provides
    fun provideTaskRepository(
        @ApplicationContext context: Context,
        taskDao: TaskDao,
        grpcTaskDataSource: GrpcTaskDataSource,
    ): TaskRepository = TaskRepositoryImpl(
        WorkManager.getInstance(context),
        taskDao,
        grpcTaskDataSource
    )

    @Singleton
    @Provides
    fun provideShareAgreementRepository(dataShareAgreementDao: ShareAgreementDao): ShareAgreementRepository =
        ShareAgreementRepositoryImpl(dataShareAgreementDao)
}
