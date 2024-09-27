package researchstack.domain.usecase.sensor

import researchstack.domain.model.sensor.TrackerDataType
import researchstack.domain.repository.sensor.AcceleroRepository
import researchstack.domain.repository.sensor.LightRepository
import researchstack.domain.repository.sensor.SpeedRepository
import javax.inject.Inject

class SyncTrackerDataUseCase @Inject constructor(
    private val getSensorTypeStudyIdsMapUseCase: GetSensorTypeStudyIdsMapUseCase,
    private val acceleroRepository: AcceleroRepository,
    private val lightRepository: LightRepository,
    private val speedRepository: SpeedRepository,
) {
    suspend operator fun invoke() {
        val typeStudyIdsMap = getSensorTypeStudyIdsMapUseCase()
        TrackerDataType.values().forEach { type -> typeStudyIdsMap[type]?.let { type.sync(it) } }
    }

    private suspend fun TrackerDataType.sync(studyIds: List<String>) {
        when (this) {
            TrackerDataType.LIGHT -> lightRepository.sync(studyIds)
            TrackerDataType.ACCELEROMETER -> acceleroRepository.sync(studyIds)
            TrackerDataType.SPEED -> speedRepository.sync(studyIds)
        }
    }
}
