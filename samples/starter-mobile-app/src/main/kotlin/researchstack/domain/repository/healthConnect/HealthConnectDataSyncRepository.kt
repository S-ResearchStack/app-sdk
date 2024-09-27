package researchstack.domain.repository.healthConnect

interface HealthConnectDataSyncRepository {
    suspend fun syncHealthData()
}
