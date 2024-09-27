package researchstack.data.local.room.dao

import androidx.room.Dao
import researchstack.domain.model.priv.BIA_TABLE_NAME
import researchstack.domain.model.priv.Bia

@Dao
abstract class BiaDao : PrivDao<Bia>(BIA_TABLE_NAME)
