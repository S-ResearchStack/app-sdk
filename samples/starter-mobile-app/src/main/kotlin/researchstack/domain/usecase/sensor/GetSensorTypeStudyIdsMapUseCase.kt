package researchstack.domain.usecase.sensor

import researchstack.domain.repository.sensor.PermittedSensorTypeRepository
import javax.inject.Inject

class GetSensorTypeStudyIdsMapUseCase @Inject constructor(
    private val permittedSensorTypeRepository: PermittedSensorTypeRepository
) {
    suspend operator fun invoke() = permittedSensorTypeRepository.getPermittedTypeStudyIdMap()
}
