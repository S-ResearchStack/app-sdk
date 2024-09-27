package researchstack.config.provider

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import researchstack.BuildConfig
import researchstack.auth.data.repository.auth.AuthRepositoryWrapper
import researchstack.auth.domain.repository.AccountRepository
import researchstack.auth.domain.usecase.GetAccountUseCase
import researchstack.backend.integration.GrpcHealthDataSynchronizer
import researchstack.backend.integration.outport.AppLogOutPort
import researchstack.backend.integration.outport.FileOutPort
import researchstack.backend.integration.outport.HealthDataOutPort
import researchstack.backend.integration.outport.StudyDataOutPort
import researchstack.backend.integration.outport.StudyOutPort
import researchstack.backend.integration.outport.TaskOutPort
import researchstack.data.datasource.grpc.GrpcFileDataSource
import researchstack.data.datasource.grpc.GrpcHealthDataSynchronizerImpl
import researchstack.data.datasource.grpc.GrpcStudyDataSource
import researchstack.data.datasource.grpc.GrpcTaskDataSource
import researchstack.data.datasource.grpc.interceptor.IdTokenProvideInterceptor
import researchstack.data.datasource.grpc.interceptor.LoggingInterceptor
import researchstack.data.datasource.http.RetrofitClient
import researchstack.data.datasource.local.file.FileManager
import researchstack.data.datasource.local.room.dao.LogDao
import researchstack.data.repository.FileRepositoryImpl
import researchstack.data.repository.LogRepositoryImpl
import researchstack.data.repository.StudyDataRepositoryImpl
import researchstack.domain.model.shealth.HealthDataModel
import researchstack.domain.repository.FileRepository
import researchstack.domain.repository.LogRepository
import researchstack.domain.repository.StudyDataRepository
import researchstack.domain.usecase.log.AppLogger
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
                intercept(LoggingInterceptor())
            }
            .build()

    @Singleton
    @Provides
    @Named("channelNoIdToken")
    fun provideManagedChannelWithNoIdToken(): ManagedChannel =
        ManagedChannelBuilder.forAddress(SERVER_ADDRESS, SERVER_PORT)
            .executor(Dispatchers.IO.asExecutor())
            .apply {
                if (BuildConfig.USE_PLAIN_TEXT) {
                    usePlaintext()
                }
                intercept(LoggingInterceptor())
            }
            .build()

    @Singleton
    @Provides
    fun provideGrpcTaskDataSource(taskOutPort: TaskOutPort): GrpcTaskDataSource =
        GrpcTaskDataSource(taskOutPort)

    @Singleton
    @Provides
    fun provideGrpcFileDataSource(fileOutPort: FileOutPort): GrpcFileDataSource = GrpcFileDataSource(fileOutPort)

    @Singleton
    @Provides
    fun provideGrpcStudyDataSource(studyOutPort: StudyOutPort): GrpcStudyDataSource =
        GrpcStudyDataSource(studyOutPort)

    @Singleton
    @Provides
    fun provideGrpcHealthDataSynchronizer(healthDataOutPort: HealthDataOutPort): GrpcHealthDataSynchronizer<HealthDataModel> =
        GrpcHealthDataSynchronizerImpl(healthDataOutPort)

    @Singleton
    @Provides
    fun provideStudyDataRepository(studyDataOutPort: StudyDataOutPort): StudyDataRepository =
        StudyDataRepositoryImpl(studyDataOutPort)

    @Singleton
    @Provides
    fun provideFileRepository(
        grpcFileDataSource: GrpcFileDataSource,
        @ApplicationContext context: Context,
    ): FileRepository =
        FileRepositoryImpl(
            grpcFileDataSource,
            RetrofitClient.initialize().let { RetrofitClient.getInstance().getAPI() },
            FileManager(context)
        )

    @Singleton
    @Provides
    fun provideLogRepository(
        appLogOutPort: AppLogOutPort,
        logDao: LogDao,
        accountRepository: AccountRepository,
    ): LogRepository = LogRepositoryImpl(
        appLogOutPort,
        logDao,
        GetAccountUseCase(accountRepository)
    ).also {
        // FIXME LogRepositoryImpl: It would be better to separate the functions of save log and upload log.
        AppLogger.initialize(it)
    }
}
