package researchstack.data.datasource.grpc

import researchstack.backend.integration.GrpcHealthDataSynchronizer
import researchstack.backend.integration.outport.HealthDataOutPort
import researchstack.data.datasource.grpc.mapper.toData
import researchstack.domain.model.shealth.HealthDataModel
import javax.inject.Inject

class GrpcHealthDataSynchronizerImpl @Inject constructor(
    private val healthDataOutPort: HealthDataOutPort,
) : GrpcHealthDataSynchronizer<HealthDataModel> {
    override suspend fun syncHealthData(studyIds: List<String>, healthData: HealthDataModel): Result<Unit> = healthDataOutPort.syncHealthData(studyIds, healthData.toData())

    override suspend fun syncBatchHealthData(studyIds: List<String>, batchHealthData: List<HealthDataModel>): Result<Unit> = healthDataOutPort.syncBatchHealthData(studyIds, batchHealthData.map { it.toData() })
}
