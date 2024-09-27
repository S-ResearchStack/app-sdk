package researchstack.data.repository.sensor

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import researchstack.backend.integration.GrpcHealthDataSynchronizer
import researchstack.data.datasource.local.room.dao.TimestampEntityBaseDao
import researchstack.data.datasource.local.room.entity.Speed
import researchstack.data.datasource.sensor.LocationTracker
import researchstack.domain.model.shealth.HealthDataModel
import researchstack.domain.repository.sensor.SpeedRepository

class SpeedRepositoryImpl(
    context: Context,
    override val timestampEntityBaseDao: TimestampEntityBaseDao<Speed>,
    override val grpcHealthDataSynchronizer: GrpcHealthDataSynchronizer<HealthDataModel>,
    dataStore: DataStore<Preferences>
) : SpeedRepository, LocationTracker(
    context, dataStore, timestampEntityBaseDao, grpcHealthDataSynchronizer
)
