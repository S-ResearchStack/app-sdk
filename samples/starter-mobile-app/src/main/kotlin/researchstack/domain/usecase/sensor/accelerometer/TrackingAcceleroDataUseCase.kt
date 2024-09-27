package researchstack.domain.usecase.sensor.accelerometer

import researchstack.domain.repository.sensor.AcceleroRepository
import javax.inject.Inject

class TrackingAcceleroDataUseCase @Inject constructor(private val acceleroRepository: AcceleroRepository) {
    operator fun invoke() = acceleroRepository.startTracking()
}
