package researchstack.domain.usecase.sensor.light

import researchstack.domain.model.sensor.Light
import researchstack.domain.repository.sensor.LightRepository
import javax.inject.Inject

class SaveLightDataUseCase @Inject constructor(private val lightRepository: LightRepository) {
    operator fun invoke(data: List<Light>) = lightRepository.insertAll(*data.toTypedArray())
}
