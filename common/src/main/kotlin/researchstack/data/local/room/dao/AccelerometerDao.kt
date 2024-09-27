package researchstack.data.local.room.dao

import androidx.room.Dao
import researchstack.domain.model.priv.ACCELEROMETER_TABLE_NAME
import researchstack.domain.model.priv.Accelerometer

@Dao
abstract class AccelerometerDao : PrivDao<Accelerometer>(ACCELEROMETER_TABLE_NAME)
