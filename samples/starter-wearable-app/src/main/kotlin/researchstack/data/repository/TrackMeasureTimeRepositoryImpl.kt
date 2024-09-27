package researchstack.data.repository

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import researchstack.data.local.pref.TrackMeasureTimePref
import researchstack.domain.repository.TrackMeasureTimeRepository

class TrackMeasureTimeRepositoryImpl(
    private val trackMeasureTimePref: TrackMeasureTimePref
) : TrackMeasureTimeRepository {
    override suspend fun add(prefKey: Preferences.Key<Long>, value: Long) {
        trackMeasureTimePref.add(prefKey, value)
    }

    override suspend fun getLastMeasureTime(prefKey: Preferences.Key<Long>): Flow<Long> =
        trackMeasureTimePref.getLastMeasureFlow(prefKey)
}
