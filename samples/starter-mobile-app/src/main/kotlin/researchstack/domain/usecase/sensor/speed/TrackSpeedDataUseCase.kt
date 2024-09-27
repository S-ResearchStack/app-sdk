package researchstack.domain.usecase.sensor.speed

import kotlinx.coroutines.flow.Flow
import researchstack.data.datasource.local.room.entity.Speed
import researchstack.domain.repository.sensor.SpeedRepository
import javax.inject.Inject

class TrackSpeedDataUseCase @Inject constructor(private val speedRepository: SpeedRepository) {
    operator fun invoke(): Flow<Speed> = speedRepository.startTracking()
}
