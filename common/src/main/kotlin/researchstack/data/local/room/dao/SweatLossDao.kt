package researchstack.data.local.room.dao

import androidx.room.Dao
import researchstack.domain.model.priv.SWEAT_LOSS_TABLE_NAME
import researchstack.domain.model.priv.SweatLoss

@Dao
abstract class SweatLossDao : PrivDao<SweatLoss>(SWEAT_LOSS_TABLE_NAME)
