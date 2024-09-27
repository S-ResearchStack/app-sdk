package researchstack.data.repository.wearable

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import researchstack.data.local.room.dao.PassiveDataStatusDao
import researchstack.data.local.room.entity.PassiveDataStatusEntity
import researchstack.domain.model.priv.PrivDataType
import researchstack.domain.repository.PassiveDataStatusRepository
import javax.inject.Inject

class PassiveDataStatusRepositoryImpl @Inject constructor(
    private val passiveDataStatusDao: PassiveDataStatusDao,
) : PassiveDataStatusRepository {
    override fun getStatus(): Flow<List<PassiveDataStatusEntity>> = callbackFlow {
        passiveDataStatusDao.getEnabledData().collect {
            val listStatus = ArrayList<PassiveDataStatusEntity>()
            it.forEach { entity ->
                listStatus.add(entity)
            }

            trySend(listStatus)
        }
    }

    override suspend fun setStatus(dataType: PrivDataType, status: Boolean): Boolean {
        if (passiveDataStatusDao.isEnable(dataType.name) != status) {
            passiveDataStatusDao.save(PassiveDataStatusEntity(dataType.name, status))
            return true
        }
        return false
    }
}
