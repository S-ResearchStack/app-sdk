package researchstack.data.local.room.dao

import androidx.room.Dao
import researchstack.domain.model.priv.PPG_IR_TABLE_NAME
import researchstack.domain.model.priv.PpgIr

@Dao
abstract class PpgIrDao : PrivDao<PpgIr>(PPG_IR_TABLE_NAME)
