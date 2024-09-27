package researchstack.domain.usecase.sensor.accelerometer

import researchstack.domain.model.sensor.Accelerometer
import researchstack.domain.repository.sensor.AcceleroRepository
import javax.inject.Inject

class SaveAcceleroDataUseCase @Inject constructor(private val acceleroRepository: AcceleroRepository) {
    operator fun invoke(data: List<Accelerometer>) = acceleroRepository.insertAll(*data.toTypedArray())
}
