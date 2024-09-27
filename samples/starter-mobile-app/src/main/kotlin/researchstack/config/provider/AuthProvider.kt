package researchstack.config.provider

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import researchstack.auth.data.datasource.auth.supertokens.SuperTokensAuthRequester
import researchstack.auth.data.repository.auth.supertokens.SuperTokensAuthRepository
import researchstack.auth.domain.repository.AuthRepository
import researchstack.backend.integration.outport.SubjectOutPort
import researchstack.data.datasource.local.room.ResearchAppDatabase
import researchstack.data.datasource.local.room.dao.StudyDao
import researchstack.data.repository.LocalDBRepositoryImpl
import researchstack.data.repository.ProfileRepositoryImpl
import researchstack.data.repository.UserStatusRepositoryImpl
import researchstack.domain.repository.LocalDBRepository
import researchstack.domain.repository.ProfileRepository
import researchstack.domain.repository.UserStatusRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthProvider {
    @Singleton
    @Provides
    fun provideLocalDBRepository(researchAppDatabase: ResearchAppDatabase): LocalDBRepository =
        LocalDBRepositoryImpl(researchAppDatabase)

    @Singleton
    @Provides
    fun provideGrpcProfileRepository(subjectOutPort: SubjectOutPort): ProfileRepository =
        ProfileRepositoryImpl(subjectOutPort)

    @Singleton
    @Provides
    fun provideGrpcUserStatusRepository(studyDao: StudyDao, subjectOutPort: SubjectOutPort): UserStatusRepository =
        UserStatusRepositoryImpl(studyDao, subjectOutPort)

    @Singleton
    @Provides
    fun provideAuthRepository(
        superTokensAuthRequester: SuperTokensAuthRequester,
    ): AuthRepository =
        SuperTokensAuthRepository(superTokensAuthRequester)
}
