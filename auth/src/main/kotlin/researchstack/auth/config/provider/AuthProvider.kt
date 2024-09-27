package researchstack.auth.config.provider

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.grpc.ManagedChannel
import researchstack.auth.data.datasource.auth.supertokens.SuperTokensAuthRequester
import researchstack.auth.data.datasource.local.pref.AccountPref
import researchstack.auth.data.datasource.local.pref.IdTokenPref
import researchstack.auth.data.datasource.local.pref.dataStore
import researchstack.auth.data.repository.account.AccountPrefRepositoryImpl
import researchstack.auth.data.repository.auth.AuthRepositoryWrapper
import researchstack.auth.data.repository.idtoken.IdTokenPrefRepositoryImpl
import researchstack.auth.domain.repository.AccountRepository
import researchstack.auth.domain.repository.AuthRepository
import researchstack.auth.domain.repository.IdTokenRepository
import researchstack.backend.grpc.AuthServiceGrpcKt
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthProvider {
    @Singleton
    @Provides
    fun provideAccountRepository(@ApplicationContext context: Context): AccountRepository =
        AccountPrefRepositoryImpl(AccountPref(context.dataStore))

    @Singleton
    @Provides
    fun provideIdTokenRepository(@ApplicationContext context: Context): IdTokenRepository =
        IdTokenPrefRepositoryImpl(IdTokenPref(context.dataStore))

    @Singleton
    @Provides
    fun provideAuthRepositoryWrapper(
        @ApplicationContext context: Context,
        authRepository: AuthRepository,
    ): AuthRepositoryWrapper =
        AuthRepositoryWrapper(context, authRepository)

    @Singleton
    @Provides
    fun provideSuperTokensRequester(
        @Named("channelNoIdToken") channel: ManagedChannel,
    ): SuperTokensAuthRequester = SuperTokensAuthRequester(
        AuthServiceGrpcKt.AuthServiceCoroutineStub(channel)
    )
}
