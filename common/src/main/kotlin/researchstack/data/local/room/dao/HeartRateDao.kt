package researchstack.data.local.room.dao

import androidx.room.Dao
import researchstack.domain.model.priv.HEART_TABLE_NAME
import researchstack.domain.model.priv.HeartRate

@Dao
abstract class HeartRateDao : PrivDao<HeartRate>(HEART_TABLE_NAME)
