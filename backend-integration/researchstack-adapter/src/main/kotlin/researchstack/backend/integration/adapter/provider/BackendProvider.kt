package researchstack.backend.integration.adapter.provider

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.grpc.ManagedChannel
import researchstack.backend.grpc.AppLogServiceGrpcKt
import researchstack.backend.grpc.FileServiceGrpcKt
import researchstack.backend.grpc.HealthDataServiceGrpcKt
import researchstack.backend.grpc.StudyDataServiceGrpcKt
import researchstack.backend.grpc.StudyServiceGrpcKt
import researchstack.backend.grpc.SubjectGrpcKt
import researchstack.backend.grpc.TaskServiceGrpcKt
import researchstack.backend.integration.adapter.outgoing.AppLogAdapter
import researchstack.backend.integration.adapter.outgoing.FileAdapter
import researchstack.backend.integration.adapter.outgoing.HealthDataAdapter
import researchstack.backend.integration.adapter.outgoing.StudyAdapter
import researchstack.backend.integration.adapter.outgoing.StudyDataAdapter
import researchstack.backend.integration.adapter.outgoing.SubjectAdapter
import researchstack.backend.integration.adapter.outgoing.TaskAdapter
import researchstack.backend.integration.outport.AppLogOutPort
import researchstack.backend.integration.outport.FileOutPort
import researchstack.backend.integration.outport.HealthDataOutPort
import researchstack.backend.integration.outport.StudyDataOutPort
import researchstack.backend.integration.outport.StudyOutPort
import researchstack.backend.integration.outport.SubjectOutPort
import researchstack.backend.integration.outport.TaskOutPort
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BackendProvider {
    @Singleton
    @Provides
    fun provideAppLogService(@Named("channelNoIdToken") channel: ManagedChannel): AppLogOutPort =
        AppLogAdapter(AppLogServiceGrpcKt.AppLogServiceCoroutineStub(channel))

    @Singleton
    @Provides
    fun provideFileService(channel: ManagedChannel): FileOutPort = FileAdapter(FileServiceGrpcKt.FileServiceCoroutineStub(channel))

    @Singleton
    @Provides
    fun provideHealthDataService(channel: ManagedChannel): HealthDataOutPort = HealthDataAdapter(
        HealthDataServiceGrpcKt.HealthDataServiceCoroutineStub(channel)
    )

    @Singleton
    @Provides
    fun provideStudyDataService(channel: ManagedChannel): StudyDataOutPort = StudyDataAdapter(
        StudyDataServiceGrpcKt.StudyDataServiceCoroutineStub(channel)
    )

    @Singleton
    @Provides
    fun provideStudyService(channel: ManagedChannel): StudyOutPort = StudyAdapter(
        StudyServiceGrpcKt.StudyServiceCoroutineStub(channel)
    )

    @Singleton
    @Provides
    fun provideTaskService(channel: ManagedChannel): TaskOutPort = TaskAdapter(
        TaskServiceGrpcKt.TaskServiceCoroutineStub(channel)
    )

    @Singleton
    @Provides
    fun provideGrpcSubjectStatusSource(channel: ManagedChannel): SubjectOutPort = SubjectAdapter(
        SubjectGrpcKt.SubjectCoroutineStub(channel)
    )
}
