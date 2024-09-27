package researchstack.data.local.room.dao

import androidx.room.Dao
import researchstack.domain.model.priv.ECG_TABLE_NAME
import researchstack.domain.model.priv.EcgSet

@Dao
abstract class EcgDao : PrivDao<EcgSet>(ECG_TABLE_NAME)
