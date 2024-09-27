package researchstack.domain.usecase.healthConnect

import researchstack.domain.repository.healthConnect.HealthConnectDataSyncRepository
import javax.inject.Inject

class SyncHealthConnectDataUseCase @Inject constructor(private val repository: HealthConnectDataSyncRepository) {
    suspend operator fun invoke() = repository.syncHealthData()
}
