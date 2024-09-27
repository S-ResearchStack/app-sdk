package researchstack.data.datasource.local.room.dao

import androidx.room.Dao
import researchstack.domain.model.sensor.LIGHT_TABLE_NAME
import researchstack.domain.model.sensor.Light

@Dao
abstract class LightDao : TimestampEntityBaseDao<Light>(LIGHT_TABLE_NAME)
