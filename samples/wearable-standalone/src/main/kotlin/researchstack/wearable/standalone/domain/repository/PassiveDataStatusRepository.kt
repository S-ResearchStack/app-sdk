package researchstack.wearable.standalone.domain.repository

import kotlinx.coroutines.flow.Flow
import researchstack.data.local.room.entity.PassiveDataStatusEntity
import researchstack.domain.model.priv.PrivDataType

interface PassiveDataStatusRepository {
    fun getStatus(): Flow<List<PassiveDataStatusEntity>>
    suspend fun setStatus(dataType: PrivDataType, status: Boolean)
}
