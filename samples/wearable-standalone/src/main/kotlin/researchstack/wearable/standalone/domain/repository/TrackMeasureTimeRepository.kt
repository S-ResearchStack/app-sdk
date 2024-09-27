package researchstack.wearable.standalone.domain.repository

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface TrackMeasureTimeRepository {
    suspend fun add(prefKey: Preferences.Key<Long>, value: Long)
    suspend fun getLastMeasureTime(prefKey: Preferences.Key<Long>): Flow<Long>
}
