package researchstack.backend.integration

interface GrpcHealthDataSynchronizer<T : HealthDataModelInterface> {
    suspend fun syncHealthData(studyIds: List<String>, healthData: T): Result<Unit>

    suspend fun syncBatchHealthData(
        studyIds: List<String>,
        batchHealthData: List<T>
    ): Result<Unit>
}
