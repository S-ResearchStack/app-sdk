package researchstack.wearable.standalone.data.repository

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow
import researchstack.wearable.standalone.data.local.pref.TrackMeasureTimePref
import researchstack.wearable.standalone.domain.repository.TrackMeasureTimeRepository

class TrackMeasureTimeRepositoryImpl(
    private val trackMeasureTimePref: TrackMeasureTimePref
) : TrackMeasureTimeRepository {
    override suspend fun add(prefKey: Preferences.Key<Long>, value: Long) {
        trackMeasureTimePref.add(prefKey, value)
    }

    override suspend fun getLastMeasureTime(prefKey: Preferences.Key<Long>): Flow<Long> =
        trackMeasureTimePref.getLastMeasureFlow(prefKey)
}
