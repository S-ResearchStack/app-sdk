package researchstack.domain.usecase

import androidx.datastore.preferences.core.Preferences
import researchstack.domain.repository.TrackMeasureTimeRepository
import javax.inject.Inject

class TrackMeasureTimeUseCase @Inject constructor(
    private val trackMeasureTimeRepository: TrackMeasureTimeRepository
) {
    suspend operator fun invoke(prefKey: Preferences.Key<Long>) = trackMeasureTimeRepository.getLastMeasureTime(prefKey)
    suspend operator fun invoke(prefKey: Preferences.Key<Long>, value: Long) =
        trackMeasureTimeRepository.add(prefKey, value)
}
