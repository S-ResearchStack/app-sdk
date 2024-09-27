package researchstack.domain.usecase.sensor

import researchstack.domain.repository.sensor.PermittedSensorTypeRepository
import javax.inject.Inject

class GetPermittedSensorTypesUseCase @Inject constructor(
    private val permittedSensorTypeRepository: PermittedSensorTypeRepository
) {
    operator fun invoke() = permittedSensorTypeRepository.getPermittedTypes()
}
