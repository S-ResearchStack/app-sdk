package researchstack.data.repository.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import researchstack.backend.integration.GrpcHealthDataSynchronizer
import researchstack.data.datasource.local.pref.SyncTimePref.SyncTimePrefKey
import researchstack.data.datasource.local.room.dao.TimestampEntityBaseDao
import researchstack.data.datasource.sensor.SensorTracker
import researchstack.domain.model.sensor.Light
import researchstack.domain.model.sensor.TrackerDataType
import researchstack.domain.model.shealth.HealthDataModel
import researchstack.domain.repository.sensor.LightRepository
import javax.inject.Inject

class LightRepositoryImpl @Inject constructor(
    sensorManager: SensorManager,
    override val timestampEntityBaseDao: TimestampEntityBaseDao<Light>,
    override val grpcHealthDataSynchronizer: GrpcHealthDataSynchronizer<HealthDataModel>,
    dataStore: DataStore<Preferences>,
) : LightRepository,
    SensorTracker<Light>(sensorManager, Sensor.TYPE_LIGHT, dataStore) {
    override val syncTimePrefKey: SyncTimePrefKey = SyncTimePrefKey.LIGHT_SYNC
    override val trackerDataType: TrackerDataType = TrackerDataType.LIGHT
    override fun SensorEvent.toModel(accuracy: Int): Light = Light(accuracy, values[0].toInt())
}
