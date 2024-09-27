package researchstack.domain.usecase.sensor.speed

import researchstack.data.datasource.local.room.entity.Speed
import researchstack.domain.repository.sensor.SpeedRepository
import javax.inject.Inject

class SaveSpeedDataUseCase @Inject constructor(private val speedRepository: SpeedRepository) {
    operator fun invoke(data: List<Speed>) = speedRepository.insertAll(*data.toTypedArray())
}
