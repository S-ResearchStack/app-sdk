package researchstack.backend.integration.adapter.outgoing

import researchstack.backend.grpc.BatchHealthDataSyncRequest
import researchstack.backend.grpc.HealthData
import researchstack.backend.grpc.HealthDataServiceGrpcKt
import researchstack.backend.grpc.HealthDataSyncRequest
import researchstack.backend.integration.outport.HealthDataOutPort
import javax.inject.Inject

class HealthDataAdapter @Inject constructor(
    private val healthDataServiceCoroutineStub: HealthDataServiceGrpcKt.HealthDataServiceCoroutineStub
) : HealthDataOutPort {
    override suspend fun syncHealthData(studyIds: List<String>, healthData: HealthData): Result<Unit> = runCatching {
        healthDataServiceCoroutineStub.syncHealthData(
            HealthDataSyncRequest.newBuilder().setHealthData(healthData).addAllStudyIds(studyIds).build()
        )
    }

    override suspend fun syncBatchHealthData(studyIds: List<String>, batchHealthData: List<HealthData>): Result<Unit> = runCatching {
        healthDataServiceCoroutineStub.syncBatchHealthData(
            BatchHealthDataSyncRequest.newBuilder()
                .addAllHealthData(batchHealthData)
                .addAllStudyIds(studyIds)
                .build()
        )
    }
}
