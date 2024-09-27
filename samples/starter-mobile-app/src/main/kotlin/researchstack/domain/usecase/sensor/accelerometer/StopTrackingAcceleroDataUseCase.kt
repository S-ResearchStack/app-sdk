package researchstack.domain.usecase.sensor.accelerometer

import researchstack.domain.repository.sensor.AcceleroRepository
import javax.inject.Inject

class StopTrackingAcceleroDataUseCase @Inject constructor(private val acceleroRepository: AcceleroRepository) {
    operator fun invoke() = acceleroRepository.stopTracking()
}
