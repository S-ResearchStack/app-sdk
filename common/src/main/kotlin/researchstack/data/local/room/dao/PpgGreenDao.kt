package researchstack.data.local.room.dao

import androidx.room.Dao
import researchstack.domain.model.priv.PPG_GREEN_TABLE_NAME
import researchstack.domain.model.priv.PpgGreen

@Dao
abstract class PpgGreenDao : PrivDao<PpgGreen>(PPG_GREEN_TABLE_NAME)
