package researchstack.wearable.standalone.data.datasource.grpc

import researchstack.backend.grpc.BatchHealthDataSyncRequest
import researchstack.backend.grpc.HealthDataServiceGrpcKt.HealthDataServiceCoroutineStub
import researchstack.backend.grpc.HealthDataSyncRequest
import researchstack.backend.integration.GrpcHealthDataSynchronizer
import researchstack.domain.model.shealth.HealthDataModel
import researchstack.wearable.standalone.data.datasource.grpc.mapper.toData

class GrpcHealthDataSynchronizerImpl(private val healthDataServiceStub: HealthDataServiceCoroutineStub) :
    GrpcHealthDataSynchronizer<HealthDataModel> {
    override suspend fun syncHealthData(studyIds: List<String>, healthData: HealthDataModel): Result<Unit> =
        runCatching {
            healthDataServiceStub.syncHealthData(
                HealthDataSyncRequest.newBuilder().setHealthData(healthData.toData()).addAllStudyIds(studyIds).build()
            )
        }

    override suspend fun syncBatchHealthData(
        studyIds: List<String>,
        batchHealthData: List<HealthDataModel>
    ): Result<Unit> =
        runCatching {
            healthDataServiceStub.syncBatchHealthData(
                BatchHealthDataSyncRequest.newBuilder()
                    .addAllHealthData(batchHealthData.map { it.toData() })
                    .addAllStudyIds(studyIds)
                    .build()
            )
        }
}
