package researchstack.data.datasource.local.room.dao

import androidx.room.Dao
import researchstack.data.datasource.local.room.entity.SPEED_TABLE_NAME
import researchstack.data.datasource.local.room.entity.Speed

@Dao
abstract class SpeedDao : TimestampEntityBaseDao<Speed>(SPEED_TABLE_NAME)
