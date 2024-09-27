package researchstack.data.local.room.dao

import androidx.room.Dao
import researchstack.domain.model.priv.PPG_RED_TABLE_NAME
import researchstack.domain.model.priv.PpgRed

@Dao
abstract class PpgRedDao : PrivDao<PpgRed>(PPG_RED_TABLE_NAME)
