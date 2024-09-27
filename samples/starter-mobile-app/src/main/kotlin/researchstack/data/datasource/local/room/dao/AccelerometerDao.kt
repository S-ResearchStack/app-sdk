package researchstack.data.datasource.local.room.dao

import androidx.room.Dao
import researchstack.domain.model.sensor.ACCELEROMETER_TABLE_NAME
import researchstack.domain.model.sensor.Accelerometer

@Dao
abstract class AccelerometerDao : TimestampEntityBaseDao<Accelerometer>(ACCELEROMETER_TABLE_NAME)
