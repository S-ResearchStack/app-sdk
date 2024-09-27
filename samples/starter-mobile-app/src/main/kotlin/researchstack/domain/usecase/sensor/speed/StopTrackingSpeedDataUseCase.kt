package researchstack.domain.usecase.sensor.speed

import researchstack.domain.repository.sensor.SpeedRepository
import javax.inject.Inject

class StopTrackingSpeedDataUseCase @Inject constructor(private val speedRepository: SpeedRepository) {
    operator fun invoke() = speedRepository.stopTracking()
}
