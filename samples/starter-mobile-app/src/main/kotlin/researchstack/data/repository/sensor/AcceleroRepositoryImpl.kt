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
import researchstack.domain.model.sensor.Accelerometer
import researchstack.domain.model.sensor.TrackerDataType
import researchstack.domain.model.shealth.HealthDataModel
import researchstack.domain.repository.sensor.AcceleroRepository
import javax.inject.Inject

class AcceleroRepositoryImpl @Inject constructor(
    sensorManager: SensorManager,
    override val timestampEntityBaseDao: TimestampEntityBaseDao<Accelerometer>,
    override val grpcHealthDataSynchronizer: GrpcHealthDataSynchronizer<HealthDataModel>,
    dataStore: DataStore<Preferences>,
) : AcceleroRepository, SensorTracker<Accelerometer>(sensorManager, Sensor.TYPE_ACCELEROMETER, dataStore) {
    override val syncTimePrefKey: SyncTimePrefKey = SyncTimePrefKey.ACCELEROMETER_SYNC
    override val trackerDataType: TrackerDataType = TrackerDataType.ACCELEROMETER
    override fun SensorEvent.toModel(accuracy: Int): Accelerometer = Accelerometer(values[0], values[1], values[2])
}
