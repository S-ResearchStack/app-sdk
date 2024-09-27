package researchstack.wearable.standalone.config

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.grpc.ManagedChannel
import researchstack.auth.data.datasource.auth.supertokens.SuperTokensAuthRequester
import researchstack.auth.data.repository.auth.supertokens.SuperTokensAuthRepository
import researchstack.auth.domain.repository.AuthRepository
import researchstack.backend.grpc.SubjectGrpcKt
import researchstack.wearable.standalone.data.repository.ProfileRepositoryImpl
import researchstack.wearable.standalone.domain.repository.ProfileRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthProvider {
    @Singleton
    @Provides
    fun provideGrpcProfileRepository(channel: ManagedChannel): ProfileRepository =
        ProfileRepositoryImpl(SubjectGrpcKt.SubjectCoroutineStub(channel))

    @Singleton
    @Provides
    fun provideAuthRepository(
        superTokensAuthRequester: SuperTokensAuthRequester,
    ): AuthRepository =
        SuperTokensAuthRepository(superTokensAuthRequester)
}
