package researchstack.data.datasource.sensor

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.paging.PagingSource
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import researchstack.data.repository.RoomToServerRepository
import researchstack.domain.model.TimestampMapData
import researchstack.domain.model.sensor.TrackerDataType
import researchstack.domain.model.shealth.HealthDataModel
import researchstack.domain.repository.TimestampDataRepository

abstract class BaseTracker<T : TimestampMapData>(
    dataStore: DataStore<Preferences>,
) : TimestampDataRepository<T>, RoomToServerRepository<T>(dataStore) {
    protected abstract val trackerDataType: TrackerDataType

    @Volatile
    var isTracking = false
    lateinit var dataFlow: Flow<T>
    var sendChannel: SendChannel<T>? = null

    abstract fun startTracking(): Flow<T>

    fun stopTracking() {
        if (isTracking.not()) return
        isTracking = false
        sendChannel?.close()
        sendChannel = null
    }

    override fun insert(data: T) = timestampEntityBaseDao.insert(data)

    override fun insertAll(vararg data: T) = timestampEntityBaseDao.insertAll(*data)

    override fun deleteLEThan(timeStamp: Long) = timestampEntityBaseDao.deleteLEThan(timeStamp)

    override fun getGreaterThan(timeStamp: Long): PagingSource<Int, T> =
        timestampEntityBaseDao.getGreaterThan(timeStamp)

    override fun List<T>.toHealthDataModel() = HealthDataModel(trackerDataType, map { it.toDataMap() })
}
