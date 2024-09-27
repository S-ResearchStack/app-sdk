package researchstack.data.local.room.dao

import androidx.room.Dao
import researchstack.domain.model.priv.SPO2_TABLE_NAME
import researchstack.domain.model.priv.SpO2

@Dao
abstract class SpO2Dao : PrivDao<SpO2>(SPO2_TABLE_NAME)
