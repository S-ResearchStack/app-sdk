package researchstack.wearable.standalone.config

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import researchstack.auth.data.repository.auth.AuthRepositoryWrapper
import researchstack.backend.grpc.HealthDataServiceGrpcKt
import researchstack.backend.grpc.StudyServiceGrpcKt
import researchstack.backend.integration.GrpcHealthDataSynchronizer
import researchstack.domain.model.shealth.HealthDataModel
import researchstack.wearable.standalone.BuildConfig
import researchstack.wearable.standalone.data.datasource.grpc.GrpcHealthDataSynchronizerImpl
import researchstack.wearable.standalone.data.datasource.grpc.GrpcStudyDataSource
import researchstack.wearable.standalone.data.datasource.grpc.interceptor.IdTokenProvideInterceptor
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GrpcProvider {
    private const val SERVER_ADDRESS = BuildConfig.SERVER_ADDRESS
    private const val SERVER_PORT = BuildConfig.SERVER_PORT
    private const val USE_PLAIN_TEXT = BuildConfig.USE_PLAIN_TEXT

    @Singleton
    @Provides
    fun provideManagedChannel(authRepositoryWrapper: AuthRepositoryWrapper): ManagedChannel =
        ManagedChannelBuilder.forAddress(SERVER_ADDRESS, SERVER_PORT)
            .intercept(IdTokenProvideInterceptor(authRepositoryWrapper))
            .executor(Dispatchers.IO.asExecutor())
            .apply {
                if (BuildConfig.USE_PLAIN_TEXT) {
                    usePlaintext()
                }
            }
            .build()

    @Singleton
    @Provides
    @Named("channelNoIdToken")
    fun provideManagedChannelWithNoIdToken(): ManagedChannel =
        ManagedChannelBuilder.forAddress(SERVER_ADDRESS, SERVER_PORT)
            .executor(Dispatchers.IO.asExecutor())
            .apply {
                if (USE_PLAIN_TEXT) {
                    usePlaintext()
                }
            }
            .build()

    @Singleton
    @Provides
    fun provideGrpcStudyDataSource(channel: ManagedChannel): GrpcStudyDataSource =
        GrpcStudyDataSource(
            StudyServiceGrpcKt.StudyServiceCoroutineStub(channel)
        )

    @Singleton
    @Provides
    fun provideGrpcHealthDataSynchronizer(channel: ManagedChannel): GrpcHealthDataSynchronizer<HealthDataModel> =
        GrpcHealthDataSynchronizerImpl(
            HealthDataServiceGrpcKt.HealthDataServiceCoroutineStub(
                channel
            )
        )
}
