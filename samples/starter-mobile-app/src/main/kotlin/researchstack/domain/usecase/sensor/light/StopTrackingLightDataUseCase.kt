package researchstack.domain.usecase.sensor.light

import researchstack.domain.repository.sensor.LightRepository
import javax.inject.Inject

class StopTrackingLightDataUseCase @Inject constructor(private val lightRepository: LightRepository) {
    operator fun invoke() = lightRepository.stopTracking()
}
