package researchstack.backend.integration.outport

import researchstack.backend.grpc.HealthData

interface HealthDataOutPort {
    suspend fun syncHealthData(studyIds: List<String>, healthData: HealthData): Result<Unit>
    suspend fun syncBatchHealthData(studyIds: List<String>, batchHealthData: List<HealthData>): Result<Unit>
}
