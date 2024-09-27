package researchstack.domain.repository.sensor

import kotlinx.coroutines.flow.Flow
import researchstack.domain.model.TimestampMapData
import researchstack.domain.repository.TimestampDataRepository

interface TrackerRepository<T : TimestampMapData> : TimestampDataRepository<T> {
    fun startTracking(): Flow<T>
    fun stopTracking()
    suspend fun sync(studyIds: List<String>): Result<Unit>
}
